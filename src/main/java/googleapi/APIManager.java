package googleapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Queue;

import syncAlgorithm.FileEntry;
import syncAlgorithm.Operation;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Children;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import database.SqlConnector;

/* There will be three kinds of Manager:
 * 1. First time OAuth Manager APIManager(String accessCode)
 * 2. Calculation Manager APIManager(
 * 3. Executor Manager
 */
public class APIManager {
	/* TODO: Execution Part */
	private Drive srcDrive;
	private Drive dstDrive;
	private Drive CalculationDrive;

	String firstTimeAccessToken;
	String firstTimeRefreshToken;

	public String getFirstTimeAccessToken() {
		return firstTimeAccessToken;
	}

	public String getFirstTimeRefreshToken() {
		return firstTimeRefreshToken;
	}

	public Drive getCalculationDrive() {
		return CalculationDrive;
	}

	public Drive getSrcDrive() {
		return srcDrive;
	}

	public Drive getDstDrive() {
		return dstDrive;
	}
	
	/* For Algorithm-calculation */
	public APIManager(int srvID) {
		initExecutorDrive(Integer.toString(srvID));
	}

	/* For firstTime OAuth part */
	public APIManager(String accessCode) {
		GoogleCredential credential = GoogleAPI
				.createCredentialWithAccessCode(accessCode);
		firstTimeAccessToken = credential.getAccessToken();
		firstTimeRefreshToken = credential.getRefreshToken();
	}

	/* Algorithm-calculation */
	private void initExecutorDrive(String srvID) {
		String refreshToken = SqlConnector.getTokens(srvID);
		GoogleCredential credential = GoogleAPI.refreshToken(refreshToken);
		CalculationDrive = GoogleAPI.buildServiceByCredential(credential);
	}

	/* Algorithm-calculation */
	public List<String> listChilrenID(String folderID) {
		List<String> childIDList = new ArrayList<>();
		Children.List request = null;
		try {
			request = CalculationDrive.children().list(folderID);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		do {
			try {
				ChildList children = request.execute();

				for (ChildReference child : children.getItems()) {
					childIDList.add(child.getId());
				}
				request.setPageToken(children.getNextPageToken());
			} catch (IOException e) {
				System.out.println("An error occurred: " + e);
				request.setPageToken(null);
			}
		} while (request.getPageToken() != null
				&& request.getPageToken().length() > 0);

		return childIDList;
	}

	/* Algorithm-Calculation Part use */
	public Hashtable<String, FileEntry> getFileIDEntryTable() {
		List<File> result = new ArrayList<File>();
		Files.List request = null;
		Hashtable<String, FileEntry> idEntryTable = new Hashtable<String, FileEntry>();
		try {
			request = CalculationDrive.files().list();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

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

		for (File file : result) {
			idEntryTable.put(file.getId(), convertGoogleFileToFileEntry(file));
		}
		return idEntryTable;
	}


	private FileEntry convertGoogleFileToFileEntry(File file) {
		boolean isRoot = file.getParents().get(0).getIsRoot();
		return new FileEntry(file.getTitle(), file.getId(), file.getMimeType(),
				isRoot);
	}
	
	/* Constructor of Executor Manager */
	public APIManager(APIManager srcManager, APIManager dstManager) {
		srcDrive = srcManager.getCalculationDrive();
		dstDrive = dstManager.getCalculationDrive();
	}
	
	/* TODO: Execution Part */
	public boolean executeOperation(Queue<Operation> queue,
			Hashtable<String, FileEntry> srcPathEntryTable,
			Hashtable<String, FileEntry> dstPathEntryTable) {
		
		for(int i = 0; i < queue.size(); i++) {
			Operation op = queue.poll();
			switch(op.getType()) {
			case 1:
				GoogleAPI.download(srcDrive, op.getTargetEntry());
				break;
			case 2:
				GoogleAPI.upload(dstDrive, op.getTargetEntry(), dstPathEntryTable);
				break;
			case 3:
				GoogleAPI.deleleLocal(op.getTargetEntry());
				break;
			case 4:
				GoogleAPI.deleteRemote(dstDrive, op.getTargetEntry());
				break;
			case 5:
				FileEntry newEntry = GoogleAPI.createFolder(dstDrive, op.getTargetEntry(), dstPathEntryTable);
				dstPathEntryTable.put(newEntry.getPath(), newEntry);
				break;
			}
		}
		return true;
	}
}
