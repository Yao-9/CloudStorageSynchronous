package request;

import java.util.List;

import dataHolder.TarSrvAccInfo;

public class EditSettingReq {
	private String srvID;
	private String name;
	private List<TarSrvAccInfo> targetSrvAccInfo;
	
	public String getSrvID() {
		return srvID;
	}
	
	public String getName() {
		return name;
	}
	
	public void setSrvID(String srvID) {
		this.srvID = srvID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTargetSrvAccInfo(List<TarSrvAccInfo> targetSrvInfo) {
		this.targetSrvAccInfo = targetSrvInfo;
	}

	public List<TarSrvAccInfo> getTargetSrvAccInfo() {
		return targetSrvAccInfo;
	}
}
