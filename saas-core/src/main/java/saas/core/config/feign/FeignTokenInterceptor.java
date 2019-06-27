package saas.core.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Slf4j
@Component
public class FeignTokenInterceptor implements RequestInterceptor {
	
	public static final String TOKEN = "token";

    @Override
    public void apply(RequestTemplate requestTemplate) {
    	
    	HttpServletRequest request = getHttpServletRequest();
    		
        if(request == null){
            return;
        }
        String token = getTokenHeader(request);
        
        if(StringUtils.isBlank(token)){
        	return;
        }
        
        //将获取Token对应的值往下面传
        requestTemplate.header(TOKEN, token);
    }

    private HttpServletRequest getHttpServletRequest() {
        try {
        	
        	ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        	
        	if(attributes == null){
        		return null;
        	}
        	
        	return attributes.getRequest();            
        } catch (Exception e) {
        	log.error("getHttpServletRequest exception ",e);
            return null;
        }
    }

    /**
     * Feign拦截器拦截请求获取Token对应的值
     * @param request
     * @return
     */
    private String getTokenHeader(HttpServletRequest request) {
    	String result = null;
    	
        Enumeration<String> enumeration = request.getHeaderNames();
        
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            if(TOKEN.equals(key)){
            	result = request.getHeader(key);
            	break;
            }
        }
        return result;
    }

}