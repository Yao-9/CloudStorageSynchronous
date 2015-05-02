package syncAlgorithm;

import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

public class AccountFileDiffCalculatorTest {
	private AccountFiles srcAccountFile;
	private AccountFiles dstAccountFile;

	@Before
	public void setUp() throws Exception {
		srcAccountFile = new AccountFileGenerator(11).buildAccountFile();
		dstAccountFile = new AccountFileGenerator(12).buildAccountFile();
	}

	@Test
	public void testCalculate() {
		Queue<Operation> opQueue = new AccountFileDiffCalculator(
				srcAccountFile, dstAccountFile).calculate();
		for(Operation op : opQueue) {
			System.out.println("Op Type is " + op.getType());
			System.out.println("Op entry is " + op.getTargetEntry().getPath());
		}
	}
}
