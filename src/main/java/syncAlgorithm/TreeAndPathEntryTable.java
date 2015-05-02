package syncAlgorithm;

import java.util.Hashtable;

public class TreeAndPathEntryTable {
	private FileTree tree;
	private Hashtable<String, FileEntry> pathEntryTable;

	public FileTree getTree() {
		return tree;
	}

	public Hashtable<String, FileEntry> getPathEntryTable() {
		return pathEntryTable;
	}

	public TreeAndPathEntryTable(FileTree tree,
			Hashtable<String, FileEntry> pathEntryTable) {
		this.tree = tree;
		this.pathEntryTable = pathEntryTable;
	}

}
