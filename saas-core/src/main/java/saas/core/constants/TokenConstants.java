package saas.core.constants;

public interface TokenConstants {

    
	public static final String TOKEN_HEADER_KEY = "Token";
	
	
    // TOKEN定制的加密密钥
    public static byte[] TOKEN_KEY = new byte[]{1, -21, 23, -43, 66, -71, 95, 88};
    // 隐藏公用的参数向量
    public static byte[] COMMON_IV = new byte[]{59, 67, -32, 79, 36, -99, 89, 28};
    
    class WEB{
    	
    	public static final String TOKEN_TYPE = "web";
    	
    	// orga:web:user:token:12sd5654sdfs6efwe
    	public static final String TOKEN_REDIS_KEY = "%s:user:token:%s:%s";
    	//public static final String TOKEN_REDIS_KEY = "web:user:tokenMap";
    	
    }

    
    class APP2b{
    	
    	public static final String TOKEN_TYPE = "app2b";
    	    	
    	// orga:
    	public static final String TOKEN_REDIS_KEY = "%s:user:token:%s:%s";
    	//public static final String TOKEN_REDIS_KEY = "app:user:token2bMap";
    	
    }
    
    
}
