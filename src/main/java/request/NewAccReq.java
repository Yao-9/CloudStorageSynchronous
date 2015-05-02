package request;

import java.util.List;

import dataHolder.TarSrvAccInfo;

public class NewAccReq {
	private String accessCode;
	private String name;
	private List<TarSrvAccInfo> targetSrvAccInfo;
		
	public NewAccReq(String accessCode, String name,
			List<TarSrvAccInfo> targetSrvAccInfo) {
		this.accessCode = accessCode;
		this.name = name;
		this.targetSrvAccInfo = targetSrvAccInfo;
	}
	
	public NewAccReq() {}

	public String getAccessCode() {
		return accessCode;
	}
	
	public String getName() {
		return name;
	}
	
	public List<TarSrvAccInfo> getTargetSrvAccInfo() {
		return targetSrvAccInfo;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTargetSrvAccInfo(List<TarSrvAccInfo> targetSrvAccInfo) {
		this.targetSrvAccInfo = targetSrvAccInfo;
	}
}