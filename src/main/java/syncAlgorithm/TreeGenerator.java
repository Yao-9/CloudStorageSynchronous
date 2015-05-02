package syncAlgorithm;

import googleapi.APIManager;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class TreeGenerator {

	private APIManager manager;
	private Hashtable<String, FileEntry> idEntryTable;
	private Hashtable<String, FileEntry> pathEntryTable;
	FileTree tree;

	public TreeGenerator(APIManager manager,
			Hashtable<String, FileEntry> idEntryTable) {
		this.manager = manager;
		this.idEntryTable = idEntryTable;
		this.tree = new FileTree();
		pathEntryTable = new Hashtable<String, FileEntry>();
	}

	public TreeAndPathEntryTable build() {
		addRoot();
		addFileNodesToTree();
		TreeAndPathEntryTable result = new TreeAndPathEntryTable(tree, pathEntryTable);
		return result;
	}

	private void addRoot() {
		Set<String> fileIDSet = idEntryTable.keySet();
		for (String id : fileIDSet) {
			FileEntry file = idEntryTable.get(id);
			if (file.isRoot()) {
				file.setPath("/" + file.getName());
				tree.addToRoot(file);
			}
		}
	}

	private void addFileNodesToTree() {
		Queue<FileNode> bfsQueue = new LinkedList<FileNode>();
		
		for(FileNode e : tree.getRoot().getChildren()) {
			bfsQueue.add(e);
		}
		while (!bfsQueue.isEmpty()) {
			FileNode node = bfsQueue.poll();
			FileEntry curEntry = node.getEntry();
			if(curEntry.getFileType().equals(AlgorithmConstant.FILETYPE_FOLDER)) {
				List<String> childrenID = manager.listChilrenID(curEntry.getId());
				for(String id: childrenID) {
					FileEntry entry = idEntryTable.get(id);
					entry.setPath(curEntry.getPath() + "/" + entry.getName());
					entry.setparentPath(curEntry.getPath());
					FileNode newNode = new FileNode(entry);
					node.addChild(newNode);
					bfsQueue.add(newNode);
					pathEntryTable.put(entry.getPath(), entry);
				}
			}
		}
	}
}
