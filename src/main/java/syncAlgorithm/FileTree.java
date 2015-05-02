package syncAlgorithm;


public class FileTree {
	private FileNode root;
	
	public FileTree() {
		FileEntry fakeRootEntry = new FileEntry("/", "1", "Folder", true);
		fakeRootEntry.setPath("/");
		root = new FileNode(fakeRootEntry);
	}

	public FileNode getRoot() {
		return root;
	}
	
	public void addToRoot(FileEntry file) {
		file.setPath(root.getEntry().getPath() + file.getName());
		FileNode newNode = new FileNode(file);
		root.addChild(newNode);
	}
	
	@Override
	public String toString() {
		return root.toString(0);
	}
}
