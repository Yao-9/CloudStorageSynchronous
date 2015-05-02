package googleapi;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Hashtable;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import syncAlgorithm.FileEntry;

import com.google.api.services.drive.Drive;

public class GoogleAPITest {

//	private String accessCode = "4/GKF7RGN-YG7UrkLEgg3dSnGppvtc6KFOlTptMv4STKY.Anl9aPW9ZqkSeFc0ZRONyF6rAKjkmQI&gws_rd";
	private APIManager managerAlgSrc;
	private APIManager managerAlgDst;
	private APIManager managerExec;

	@Before
	public void setUp() throws Exception {
		managerAlgSrc = new APIManager(11);
		managerAlgDst = new APIManager(12);
		managerExec = new APIManager(managerAlgSrc, managerAlgDst);
	}

//	@Test
//	public void testCreateCredentialWithAccessCode() throws Exception {
//		GoogleCredential credential = GoogleAPI
//				.createCredentialWithAccessCode(accessCode);
//		// System.out.println(credential.getAccessToken());
//		// System.out.println(credential.getRefreshToken());
//		assertNotNull(credential.getAccessToken());
//		assertNotNull(credential.getRefreshToken());
//	}

	@Test
	public void testAlgorithmManager() throws Exception {
		Drive drive = managerAlgSrc.getCalculationDrive();
		assertNotNull(drive);
		Hashtable<String, FileEntry> table = managerAlgSrc
				.getFileIDEntryTable();
		assertTrue(table.containsKey("0BxK0BFhJuhBnRUhuNXRmMl85V2M"));
		String testFolderID = "0BxK0BFhJuhBnfmZwRnVJRDFsV2tHNDZ6RnU3SVNFREUxOGR3ZW9SMkhzOXZqV0JyUFdkV00";
		List<String> IdList = managerAlgSrc.listChilrenID(testFolderID);
		System.out.println(IdList.toString());
		String testChildId = "0BxK0BFhJuhBnflJRUWRjNUNnWFJsNWk1TkE2N2R4SXhab3RKQzlnMThvZnJsbFp6LWZnTjg";
		assertTrue(IdList.contains(testChildId));
	}

	@Test
	public void testDownloadFile() throws Exception {
		FileEntry testEntry = new FileEntry("README",
				"0BxK0BFhJuhBnZG92VlNwTGdjYzQ",
				"", false);
		GoogleAPI.download(managerExec.getSrcDrive(), testEntry);
	}
	
	// @Test
	// public void testWriteStringToFile() throws Exception {
	// String testStr = "123";
	// GoogleAPI.writeStringToFile(Constant.TEMP_PATH + "test.txt", testStr);
	// }
	
//	@Test
//	public void testUploadFile() throws Exception {
//		Drive drive = managerAlgDst.getCalculationDrive();
//		FileEntry newEntry = new FileEntry("test.jpg",
//				"0BxK0BFhJuhBnZG92VlNwTGdjYzQ",
//				"", true);
//		GoogleAPI.upload(drive, newEntry, null);
//	}
//	
//	@Test
//	public void testCreateFolder() throws Exception {
//		Drive drive = managerAlgDst.getCalculationDrive();
//		FileEntry newEntry = new FileEntry("testFolder",
//				"0BxK0BFhJuhBnZG92VlNwTGdjYzQ",
//				"", true);
//		GoogleAPI.createFolder(drive, newEntry, null);
//	}
	
	
}
