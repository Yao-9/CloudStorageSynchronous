package response;

import java.util.List;

import dataHolder.SrvAccInfo;

public class ListAccRes {
	private List<SrvAccInfo> lst;
	private String statusMsg;
	
	public ListAccRes(List<SrvAccInfo> lst, String statusMsg) {
		this.lst = lst;
		this.statusMsg = statusMsg;
	}
	
	public ListAccRes() {}

	public List<SrvAccInfo> getLst() {
		return lst;
	}

	public void setLst(List<SrvAccInfo> lst) {
		this.lst = lst;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
}
