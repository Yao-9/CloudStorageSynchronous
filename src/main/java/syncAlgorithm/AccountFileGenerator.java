package syncAlgorithm;

import java.util.Hashtable;

import googleapi.APIManager;

/* This class would build a AccountFile class using the ID */
public class AccountFileGenerator {
	private int srcID;

	public AccountFileGenerator(int srcID) {
		this.srcID = srcID;
	}

	public AccountFiles buildAccountFile() {
		APIManager manager = new APIManager(srcID);
		Hashtable<String, FileEntry> idEntryTable = manager
				.getFileIDEntryTable();
		TreeAndPathEntryTable treeAndPathEntryTable = new TreeGenerator(
				manager, idEntryTable).build();

		AccountFiles newAccountFiles = new AccountFiles(manager,
				treeAndPathEntryTable.getTree(),
				treeAndPathEntryTable.getPathEntryTable());
		return newAccountFiles;
	}
}
