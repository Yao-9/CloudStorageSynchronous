package response;

public class NewAccRes {
	private String srvAccID;
	private String statusMsg;
	
	public String getSrvAccID() {
		return srvAccID;
	}
	
	public String getStatusMsg() {
		return statusMsg;
	}
	
	public NewAccRes(String srvAccID, String statusMsg) {
		this.srvAccID = srvAccID;
		this.statusMsg = statusMsg;
	}
}
