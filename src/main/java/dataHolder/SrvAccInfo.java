package dataHolder;

public class SrvAccInfo {
	private String name;
	private String srvID;
	
	public SrvAccInfo(String name, String srvID) {
		this.name = name;
		this.srvID = srvID;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSrvID() {
		return srvID;
	}

	public void setSrvID(String srvID) {
		this.srvID = srvID;
	}

	public SrvAccInfo() {}
}
