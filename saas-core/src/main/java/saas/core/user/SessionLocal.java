package saas.core.user;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.io.Serializable;


public class SessionLocal implements Serializable {

	private static final long serialVersionUID = -8731885206822092312L;
	
	private static ThreadLocal<TokenInfo> sessionThreadLocal = new TransmittableThreadLocal<TokenInfo>();

    public static TokenInfo get() {
        return sessionThreadLocal.get();
    }

    public static void save(TokenInfo tokenInfo) {
        sessionThreadLocal.set(tokenInfo);
    }
    
    public static String getUid(){
    	TokenInfo tokenInfo = sessionThreadLocal.get();
    	
    	if(tokenInfo == null){
    		return null;
    	}
    	
    	return tokenInfo.getUid();
    }
    
    public static String getUserId(){
    	TokenInfo tokenInfo = sessionThreadLocal.get();
    	
    	if(tokenInfo == null){
    		return null;
    	}
    	
    	return tokenInfo.getUserId();
    }
    
    public static String getUserAccount(){
    	TokenInfo tokenInfo = sessionThreadLocal.get();
    	
    	if(tokenInfo == null){
    		return null;
    	}
    	
    	return tokenInfo.getUserAccount();
    }
    
    public static String getUserName(){
    	TokenInfo tokenInfo = sessionThreadLocal.get();
    	
    	if(tokenInfo == null){
    		return null;    		
    	}
    	
    	return tokenInfo.getUserName();
    }
    
    public static String getEnterpriseCode(){
    	TokenInfo tokenInfo = sessionThreadLocal.get();
    	
    	if(tokenInfo == null){
    		return null;    		
    	}
    	
    	return tokenInfo.getEnterpriseCode();
    }

    
    public static String getEnterpriseAccount(){
    	TokenInfo tokenInfo = sessionThreadLocal.get();
    	
    	if(tokenInfo == null){
    		return null;    		
    	}
    	
    	return tokenInfo.getEnterpriseAccount();
    }
    
    public static String getEnterpriseName(){
    	TokenInfo tokenInfo = sessionThreadLocal.get();
    	
    	if(tokenInfo == null){
    		return null;    		
    	}
    	
    	return tokenInfo.getEnterpriseName();
    }
    
}
