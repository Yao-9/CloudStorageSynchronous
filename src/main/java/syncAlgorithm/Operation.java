package syncAlgorithm;

/* Operation have  types:
 * Type 1: Download from source remote
 * Type 2: Upload The file under certain folder
 * Type 3: Delete temporary file on local
 * Type 4: Delete Target file on remote
 * Type 5: Create a folder
 */
public class Operation {
	private int type;
	private FileEntry targetEntry;
	
	public Operation(int type, FileEntry targetEntry) {
		this.type = type;
		this.targetEntry = targetEntry;
	}

	public int getType() {
		return type;
	}

	public FileEntry getTargetEntry() {
		return targetEntry;
	}
}
