package request;

public class ForceSyncReq {
	String srcAccID;
	String dstAccID;
	public String getSrcAccID() {
		return srcAccID;
	}
	public void setSrcAccID(String srcAccID) {
		this.srcAccID = srcAccID;
	}
	public String getDstAccID() {
		return dstAccID;
	}
	public void setDstAccID(String dstAccID) {
		this.dstAccID = dstAccID;
	}
	public ForceSyncReq(String srcAccID, String dstAccID) {
		super();
		this.srcAccID = srcAccID;
		this.dstAccID = dstAccID;
	}
	
	public ForceSyncReq() {}
}
