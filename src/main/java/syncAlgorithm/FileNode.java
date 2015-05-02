package syncAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class FileNode {
	private FileEntry entry;
	private List<FileNode> children;
	
	public List<FileNode> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<FileNode> children) {
		this.children = children;
	}

	public void setEntry(FileEntry entry) {
		this.entry = entry;
	}
	
	public FileEntry getEntry() {
		return entry;
	}

	public FileNode(FileEntry entry) {
		this.entry = entry;
		if(entry.getFileType().equals(AlgorithmConstant.FILETYPE_FOLDER));
			children = new ArrayList<>();
	}
	
	public void addChild(FileNode node) {
		children.add(node);
	}
	
	public String toString(int level) {
		String space = "";
		for (int i = 0; i < level; i++) {
			space += "  ";
		}
		
		String line = space + entry.getPath() + "\n";
		if(children != null) {
			for (FileNode node : children) {
				line += node.toString(level + 1);
			}
		}
		return line;
	}
}
