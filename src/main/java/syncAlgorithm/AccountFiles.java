package syncAlgorithm;

import java.util.Hashtable;

import googleapi.APIManager;

public class AccountFiles {

	private APIManager manager;
	private FileTree fileTree;
	private Hashtable<String, FileEntry> pathEntryTable;

	public APIManager getManager() {
		return manager;
	}

	public FileTree getFileTree() {
		return fileTree;
	}

	public Hashtable<String, FileEntry> getPathEntryTable() {
		return pathEntryTable;
	}

	public AccountFiles(APIManager manager, FileTree fileTree,
			Hashtable<String, FileEntry> pathEntryTable) {
		this.manager = manager;
		this.fileTree = fileTree;
		this.pathEntryTable = pathEntryTable;
	}
}