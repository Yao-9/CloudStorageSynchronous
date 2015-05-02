package syncAlgorithm;

import googleapi.APIManager;

import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

public class TreeGeneratorTest {
	private APIManager manager;
	@Before
	public void setUp() throws Exception {
		manager = new APIManager(11);
	}

	@Test
	public void testBuild() {
		Hashtable<String, FileEntry> idEntryTable = manager.getFileIDEntryTable();
		TreeAndPathEntryTable tapet = new TreeGenerator(manager, idEntryTable).build();
		FileTree tree = tapet.getTree();
		System.out.println(tree.toString());
		System.out.println(tapet.getPathEntryTable());
	}
}
