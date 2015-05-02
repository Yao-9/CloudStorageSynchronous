package dataHolder;

public class Token {
	
	private String src_access_token;
	private String dst_access_token;
	private String src_refresh_token;
	private String dst_refresh_token;
	
	public Token(String Src_access_token, String Dst_access_token, String Src_refresh_token, String Dst_refresh_token) {
    	this.src_access_token = Src_access_token;
    	this.dst_access_token = Dst_access_token;
    	this.src_refresh_token = Src_refresh_token;
    	this.dst_refresh_token = Dst_refresh_token;
    }
    
    public String getSrcAccessToken(){
    	return src_access_token;
    }
    public String getDstAccessToken(){
    	return dst_access_token;
    }
    public String getSrcRefreshToken(){
    	return src_refresh_token;
    }
    public String getDstRefreshToken(){
    	return dst_refresh_token;
    }
}
