package syncAlgorithm;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class AccountFileDiffCalculator {
	private AccountFiles srcAccountFile;
	private AccountFiles dstAccountFile;

	public AccountFileDiffCalculator(AccountFiles srcAccountFile,
			AccountFiles dstAccountFile) {
		this.srcAccountFile = srcAccountFile;
		this.dstAccountFile = dstAccountFile;
	}

	public Queue<Operation> calculate() {
		Queue<Operation> operationQueue = new LinkedList<>();
		Queue<FileNode> BSTQueue = new LinkedList<>();
		Hashtable<String, FileEntry> targetPathEntryTable = dstAccountFile.getPathEntryTable();

		for (FileNode e : srcAccountFile.getFileTree().getRoot().getChildren()) {
			BSTQueue.add(e);
		}

		while (!BSTQueue.isEmpty()) {
			FileNode node = BSTQueue.poll();
			FileEntry srcEntry = node.getEntry();
			if (!targetPathEntryTable.contains(srcEntry.getPath())) {
				if(srcEntry.getFileType().equals(AlgorithmConstant.FILETYPE_FOLDER)) {
					createFolderSeq(operationQueue, srcEntry);
				} else { 
					appendFileSeq(operationQueue, srcEntry);
				}
			} else {
				if (!srcEntry.getFileType().equals(AlgorithmConstant.FILETYPE_FOLDER)) {
					FileEntry targetEntry = targetPathEntryTable.get(srcEntry.getPath());
					replaceSeq(operationQueue, srcEntry, targetEntry);
				}
			}
			if (node.getChildren() != null) {
				for(FileNode childNode : node.getChildren()) {
					BSTQueue.add(childNode);
				}
			}
		}
		return operationQueue;
	}

	private void createFolderSeq(Queue<Operation> operationQueue,
			FileEntry srcEntry) {
		Operation createFolderOp = new Operation(5, srcEntry);
		operationQueue.add(createFolderOp);
	}

	private void appendFileSeq(Queue<Operation> seq, FileEntry srcEntry) {
		Operation downloadOp = new Operation(1, srcEntry);
		Operation uploadOp = new Operation(2, srcEntry);
		Operation delLocalOp = new Operation(3, srcEntry);
		seq.add(downloadOp);
		seq.add(uploadOp);
		seq.add(delLocalOp);
	}

	private void replaceSeq(Queue<Operation> seq, FileEntry srcEntry,
			FileEntry targetEntry) {
		Operation downloadOp = new Operation(1, srcEntry);
		Operation delRemoteOp = new Operation(4, targetEntry);
		Operation uploadOp = new Operation(2, srcEntry);
		Operation delLocalOp = new Operation(3, srcEntry);
		seq.add(downloadOp);
		seq.add(delRemoteOp);
		seq.add(uploadOp);
		seq.add(delLocalOp);
	}
}
