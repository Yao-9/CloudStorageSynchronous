package syncAlgorithm;


public class FileEntry {
	private String name;
	private String id;
	private String fileType;
	private String path;
	private boolean isRoot;
	private String parentPath;

	public boolean isRoot() {
		return isRoot;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public String getFileType() {
		return fileType;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
	
	public String getparentPath() {
		return this.parentPath;
	}
	
	public void setparentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public FileEntry(String name, String id, String fileType, boolean isRoot) {
		this.name = name;
		this.id = id;
		this.fileType = fileType;
		this.isRoot = isRoot;
	}
	
	public FileEntry(FileEntry another) {
		this.name = another.getName();
		this.id = another.getId();
		this.fileType = another.getFileType();
		this.isRoot = another.isRoot();
		this.parentPath = another.getparentPath();
		this.path = another.getPath();
	}


	public boolean equalTo(FileEntry fe) {
		return (this.name == fe.getName()) && (this.id == fe.getId())
				&& (this.fileType == fe.getFileType())
				&& (this.isRoot == fe.isRoot());
	}
}
