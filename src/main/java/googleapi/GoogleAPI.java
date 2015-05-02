package googleapi;

/* CloudSyncServer : GoogleAPI */
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.IOUtils;

import syncAlgorithm.AlgorithmConstant;
import syncAlgorithm.FileEntry;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Children;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;

public class GoogleAPI {
	private static HttpTransport ht = new NetHttpTransport();
	private static JsonFactory jf = new JacksonFactory();

	public static GoogleCredential refreshToken(String refreshToken) {
		TokenResponse response = null;
		try {
			response = new GoogleRefreshTokenRequest(new NetHttpTransport(),
					new JacksonFactory(), refreshToken, Constant.CLIENT_ID,
					Constant.CLIENT_SECRET).execute();
			Credential credential = new Credential(
					BearerToken.authorizationHeaderAccessMethod())
					.setFromTokenResponse(response);
			System.out.println("Access token: " + credential.getAccessToken());
		} catch (TokenResponseException e) {
			if (e.getDetails() != null) {
				System.err.println("Error: " + e.getDetails().getError());
				if (e.getDetails().getErrorDescription() != null) {
					System.err.println(e.getDetails().getErrorDescription());
				}
				if (e.getDetails().getErrorUri() != null) {
					System.err.println(e.getDetails().getErrorUri());
				}
			} else {
				System.err.println(e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return createCredentialWithRefreshToken(ht, jf, response);
	}

	private static GoogleCredential createCredentialWithRefreshToken(
			HttpTransport transport, JsonFactory jsonFactory,
			TokenResponse tokenResponse) {
		return new GoogleCredential.Builder().setTransport(transport)
				.setJsonFactory(jsonFactory)
				.setClientSecrets(Constant.CLIENT_ID, Constant.CLIENT_SECRET)
				.build().setFromTokenResponse(tokenResponse);
	}

	public static Drive buildServiceByCredential(GoogleCredential credential) {
		HttpTransport httpTransport = new NetHttpTransport();
		JacksonFactory jsonFactory = new JacksonFactory();
		return new Drive.Builder(httpTransport, jsonFactory, credential)
				.setApplicationName("Cloud Storage Client").build();
	}

	public static GoogleCredential createCredentialWithAccessCode(
			String accessCode) {
		GoogleCredential credential = null;
		try {
			GoogleAuthorizationCodeFlow flow = createAuthorizationFlow();
			GoogleTokenResponse response = flow.newTokenRequest(accessCode).execute();
			credential = createCredentialWithRefreshToken(ht, jf, response);
			System.out.println("Access token: " + response.getAccessToken());
			System.out.println("Refresh token: " + response.getRefreshToken());
		} catch (TokenResponseException e) {
			if (e.getDetails() != null) {
				System.err.println("Error: " + e.getDetails().getError());
				if (e.getDetails().getErrorDescription() != null) {
					System.err.println(e.getDetails().getErrorDescription());
				}
				if (e.getDetails().getErrorUri() != null) {
					System.err.println(e.getDetails().getErrorUri());
				}
			} else {
				System.err.println(e.getMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return credential;
	}

	private static GoogleAuthorizationCodeFlow createAuthorizationFlow() {
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow(ht,
				jf, Constant.CLIENT_ID, Constant.CLIENT_SECRET, Constant.SCOPE);
		return flow;
	}

	public static List<String> listFiles(Drive service) throws IOException {
		List<File> result = new ArrayList<File>();
		Files.List request = service.files().list();

		do {
			try {
				FileList files = request.execute();

				result.addAll(files.getItems());
				request.setPageToken(files.getNextPageToken());
			} catch (IOException e) {
				System.out.println("An error occurred: " + e);
				request.setPageToken(null);
			}
		} while (request.getPageToken() != null
				&& request.getPageToken().length() > 0);

		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < result.size(); i++) {
			ret.add(result.get(i).getId());
		}
		return ret;
	}

	public static List<String> listChildren(Drive service, String folderId)
			throws IOException {
		Children.List request = service.children().list(folderId);
		List<String> ret = new ArrayList<String>();
		do {
			try {
				ChildList children = request.execute();

				for (ChildReference child : children.getItems()) {
					ret.add(child.getId());
					System.out.println("File Id: " + child.getId());
				}
				request.setPageToken(children.getNextPageToken());
			} catch (IOException e) {
				System.out.println("An error occurred: " + e);
				request.setPageToken(null);
			}
		} while (request.getPageToken() != null
				&& request.getPageToken().length() > 0);
		return ret;
	}

	public static void download(Drive drive, FileEntry fileEntry) {
		String fileID = fileEntry.getId();
		File targetFile = null;
		InputStream in = null;

		try {
			targetFile = drive.files().get(fileID).execute();
			GenericUrl url = new GenericUrl(targetFile.getDownloadUrl());
			HttpResponse response = drive.getRequestFactory()
					.buildGetRequest(url).execute();
			in = response.getContent();
			StringWriter writer = new StringWriter();
			IOUtils.copy(response.getContent(), writer);
			writeStringToFile(Constant.TEMP_PATH + fileEntry.getName(),
					writer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void writeStringToFile(String path, String content)
			throws Exception {
		java.io.File file = new java.io.File(path);
		FileOutputStream fop = new FileOutputStream(file);
		byte[] contentInBytes = content.getBytes();
		fop.write(contentInBytes);
		fop.flush();
		fop.close();
	}

	public static void upload(Drive drive, FileEntry entry,
			Hashtable<String, FileEntry> dstPathEntryTable) {
		File body = new File();
		body.setTitle(entry.getName());
		if (!entry.isRoot()) {
			String parentID = dstPathEntryTable.get(entry.getparentPath())
					.getId();
			body.setParents(Arrays.asList(new ParentReference().setId(parentID)));
		}
		java.io.File fileContent = new java.io.File(Constant.TEMP_PATH
				+ entry.getName());
		FileContent mediaContent = new FileContent(entry.getFileType(),
				fileContent);
		try {
			File file = drive.files().insert(body, mediaContent).execute();
			System.out.println("File ID: " + file.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void deleleLocal(FileEntry entry) {
		java.io.File file = new java.io.File(Constant.TEMP_PATH
				+ entry.getName());
		file.delete();
	}

	public static void deleteRemote(Drive dstDrive, FileEntry entry) {
		try {
			dstDrive.files().trash(entry.getId()).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static FileEntry createFolder(Drive dstDrive, FileEntry entry,
			Hashtable<String, FileEntry> dstPathEntryTable) {
		File body = new File();
		body.setTitle(entry.getName());
		body.setMimeType(AlgorithmConstant.FILETYPE_FOLDER);
		File file = null;
		try {
			if (!entry.isRoot()) {
				String parentID = dstPathEntryTable.get(entry.getparentPath())
						.getId();
				body.setParents(Arrays.asList(new ParentReference()
						.setId(parentID)));
			}
			file = dstDrive.files().insert(body).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileEntry targetEntry = new FileEntry(entry);
		targetEntry.setID(file.getId());
		return targetEntry;
	}
}
