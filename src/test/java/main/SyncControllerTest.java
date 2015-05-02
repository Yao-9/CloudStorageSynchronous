package main;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import request.ForceSyncReq;
import request.NewAccReq;

public class SyncControllerTest {
	 
	private SyncController controller ;
//	private String accessCode = "4/c5XXyuAEUvObT-8TEYmn7gQxhvBG3zvp1TatzStrek4.kgUNtyaeDIcTXmXvfARQvti-6snkmQI&gws_rd";
	@Before
	public void setUp() throws Exception {
		controller = new SyncController();
	}

//	@Test
//	public void testNewAccHandler() throws ClassNotFoundException, SQLException {
//		NewAccReq nareq = new NewAccReq(accessCode, "AccountWithoutTarget",  null);
//		controller.newAccHandler(nareq);
//	}
//	
	@Test
	public void testForceSyncHandler() throws Exception {
		ForceSyncReq fsreq = new ForceSyncReq("11", "12");
		controller.forceSyncHandler(fsreq);
	}
}
