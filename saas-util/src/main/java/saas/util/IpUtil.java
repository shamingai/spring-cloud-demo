package saas.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
public class IpUtil {
    
	public static final String UNKNOWN_IP = "unknown";
	
	public static final List<String> ipHeaderList = Lists.newArrayList();
	
	static{
		ipHeaderList.add("x-forwarded-for");
		ipHeaderList.add("Proxy-Client-IP");
		ipHeaderList.add("WL-Proxy-Client-IP");
		ipHeaderList.add("HTTP_CLIENT_IP");
		ipHeaderList.add("HTTP_X_FORWARDED_FOR");
	}

	// 对于通过多个代理的情况，第一个IP为客户端真实IP,此方法返回第一个IP
	public static String getRequestIp(HttpServletRequest request){		
		return getFirstIp(parseIpAddr(request));
	}
	
	// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割,,此方法返回 IP串 
	public static String getRequestIps(HttpServletRequest request){
		return StringUtils.trim(parseIpAddr(request));
	}
	
	
    /**
     * 通过HttpServletRequest返回IP地址
     *
     * @param request HttpServletRequest
     * @return ip String
     */
    public static String parseIpAddr(HttpServletRequest request){
    	
    	String ipAddress = null;
    	
    	try {
	    	for (String key : ipHeaderList) {
	          	
	    		ipAddress = request.getHeader(key);
	            
	            if(isLegalIp(ipAddress)){
	            	return ipAddress;
	            }
	  		}
		
		} catch (Exception e) {
			log.error("parseIpAddr ", e);
		}
    	
    	return request.getRemoteAddr();    	
    }
    
    protected static boolean isLegalIp(String ip){
    	if(StringUtils.isBlank(ip)){
        	return false;
        }
    	
    	if(UNKNOWN_IP.equalsIgnoreCase(ip)){
    		return false;
    	}
    	
    	return true;
    }
    
    
    // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
    protected static String getFirstIp(String ip){
    	
    	if(StringUtils.isBlank(ip)){
        	return StringUtils.EMPTY;
        }
    	
    	if (ip.length() <= 15) {
    		return StringUtils.trim(ip);
    	}
    	
        String[] ips = ip.split(",");
        
        for (String _ip : ips) {        	
        	if (UNKNOWN_IP.equalsIgnoreCase(StringUtils.trim(_ip))) {                
                continue;
            }
        	return StringUtils.trim(_ip); 
		}
    	
    	return StringUtils.EMPTY;
    }

    
}
