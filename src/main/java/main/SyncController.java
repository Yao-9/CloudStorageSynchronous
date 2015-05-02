package main;

import googleapi.APIManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import request.EditSettingReq;
import request.ForceSyncReq;
import request.NewAccReq;
import response.ListAccRes;
import response.NewAccRes;
import response.StatusRes;
import syncAlgorithm.AccountFileDiffCalculator;
import syncAlgorithm.AccountFileGenerator;
import syncAlgorithm.AccountFiles;
import syncAlgorithm.Operation;
import dataHolder.SrvAccInfo;
import dataHolder.TarSrvAccInfo;
import database.SqlConnector;

@SpringBootApplication
@RestController
public class SyncController {

	@RequestMapping(value = "/new_acc", method = RequestMethod.POST)
	public NewAccRes newAccHandler(@RequestBody NewAccReq nareq)
			throws ClassNotFoundException, SQLException {
		String accessCode = nareq.getAccessCode();
		System.out.println(accessCode);
		APIManager manager = new APIManager(accessCode);

		String accessToken = manager.getFirstTimeAccessToken();
		String refreshToken = manager.getFirstTimeRefreshToken();

		List<TarSrvAccInfo> tsai = nareq.getTargetSrvAccInfo();
		if (tsai == null)
			System.out.println("[DEBUG] List of target srvice acc is none");
		String name = nareq.getName();
		String srvID = SqlConnector.newSrvAcct(name, accessToken, refreshToken);
		SqlConnector.newRule(srvID, tsai);
		NewAccRes nares = new NewAccRes(srvID, "Success");
		return nares;
	}

	@RequestMapping(value = "/list_srvacc", method = RequestMethod.GET)
	public @ResponseBody ListAccRes listAccHandler()
			throws ClassNotFoundException, SQLException {
		/* Reach Database to Fetch all service accounts */
		List<SrvAccInfo> lsai = SqlConnector.srvAcctList();
		ListAccRes lar = new ListAccRes(lsai, "Success");
		return lar;
	}

	@RequestMapping(value = "/edit_setting", method = RequestMethod.POST)
	public StatusRes editSettingHandler(@RequestBody EditSettingReq esreq)
			throws ClassNotFoundException, SQLException {
		/* Update the account info into database */
		String srvID = esreq.getSrvID();
		String name = esreq.getName();
		List<TarSrvAccInfo> tarSrv = esreq.getTargetSrvAccInfo();
		if (tarSrv == null) {
			System.out.println("ah-ou");
		}
		SqlConnector.updateAcct(srvID, name, tarSrv);
		StatusRes esres = new StatusRes("Success");
		return esres;
	}

	@RequestMapping(value = "/force_sync", method = RequestMethod.POST)
	public StatusRes forceSyncHandler(@RequestBody ForceSyncReq fsreq) {
		String srcID = fsreq.getSrcAccID();
		String dstID = fsreq.getDstAccID();

		AccountFiles srcAccountFiles = new AccountFileGenerator(
				Integer.valueOf(srcID)).buildAccountFile();
		AccountFiles dstAccountFiles = new AccountFileGenerator(
				Integer.valueOf(dstID)).buildAccountFile();

		Queue<Operation> queue = new AccountFileDiffCalculator(srcAccountFiles,
				dstAccountFiles).calculate();
		
		for(Operation op : queue) {
			System.out.println(op.getType());
			System.out.println(op.getTargetEntry().getName());
		}
		APIManager executionManager = new APIManager(
				srcAccountFiles.getManager(), dstAccountFiles.getManager());

		executionManager.executeOperation(queue,
				srcAccountFiles.getPathEntryTable(),
				dstAccountFiles.getPathEntryTable());
		StatusRes esres = new StatusRes("Success");
		return esres;
	}

	public static void main(String[] args) {
		SpringApplication.run(SyncController.class, args);
	}
}
