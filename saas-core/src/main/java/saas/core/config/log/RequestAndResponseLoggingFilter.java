package saas.core.config.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
public class RequestAndResponseLoggingFilter extends OncePerRequestFilter {
	
	public static final String ACTUATOR_PATH = "/actuator";

	private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
			MediaType.valueOf("text/*"),
			MediaType.APPLICATION_FORM_URLENCODED, 
			MediaType.APPLICATION_JSON, 
			MediaType.APPLICATION_XML,
			MediaType.valueOf("application/*+json"), 
			MediaType.valueOf("application/*+xml"),
			MediaType.MULTIPART_FORM_DATA);
	
	private static Map<String,LogMask> maskLogMap = Maps.newHashMap();
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		if (isAsyncDispatch(request)) {
			filterChain.doFilter(request, response);
		} else {
			doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
		}
		
	}

	protected void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		boolean isIncludePath = !excludePath(request);
		
		try {
			if(isIncludePath){
				beforeRequest(request, response);	
			}			
			filterChain.doFilter(request, response);
		} finally {
			if(isIncludePath){
				afterRequest(request, response);
			}
			response.copyBodyToResponse();

			stopWatch.stop();
			
			if(isIncludePath){
				log.info("Elapsed {} ms, {} s", stopWatch.getTotalTimeMillis(), stopWatch.getTotalTimeSeconds());
			}
		}
		
	}
		
	protected boolean excludePath(ContentCachingRequestWrapper request){
		String path = request.getRequestURI();
		
		if(StringUtils.isBlank(path)){
			return false;
		}
				
		if(path.startsWith(ACTUATOR_PATH)){
			return true;
		}
		
		return false; 
	}
	
	

	protected void beforeRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
		if (log.isInfoEnabled()) {
			logRequestHeader(request);
		}
	}

	protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
		if (log.isInfoEnabled()) {
			logRequestBody(request);
			logResponse(response);
		}
	}

	private static void logRequestHeader(ContentCachingRequestWrapper request) {
		
		String requestUIR = request.getRequestURI();
		
		if(StringUtils.isBlank(requestUIR) || requestUIR.indexOf("swagger-resources") != -1){
			return;
		}
		
		String queryString = request.getQueryString();
		
		if (queryString == null) {
			log.info("Request URI: {} {} ", request.getMethod(), request.getRequestURI());
		} else {
			log.info("Request URI: {} {}?{}", request.getMethod(), request.getRequestURI(), queryString);
		}
		
		Enumeration<String> headerNames = request.getHeaderNames();

		StringBuilder headerMsg = new StringBuilder("\r\n{\r\n");

		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			headerMsg.append(String.format("\t%s: %s\r\n", headerName, headerValue));
		}

		headerMsg.append("}\r\n");
		
		log.info("Request Headers: {}", headerMsg);

	}

	private static void logRequestBody(ContentCachingRequestWrapper request) {
		String requestUIR = request.getRequestURI();
		
		if(StringUtils.isBlank(requestUIR) || requestUIR.indexOf("swagger-resources") != -1){
			return;
		}
		
		byte[] content = request.getContentAsByteArray();
		if (content.length > 0) {
			logContent(requestUIR, "Request Body:", content, request.getContentType(), request.getCharacterEncoding());
		}
	}

	private static void logResponse(ContentCachingResponseWrapper response) {
		int status = response.getStatus();
		
		log.info("Response Status: {} {}", status, HttpStatus.valueOf(status).getReasonPhrase());

		Collection<String> headerNames = response.getHeaderNames();

		StringBuilder headerMsg = new StringBuilder("\r\n{\r\n");

		for (String headerName : headerNames) {
			String headerValue = response.getHeader(headerName);
			headerMsg.append(String.format("\t%s: %s\r\n", headerName, headerValue));
		}

		headerMsg.append("}\r\n");
				
		byte[] content = response.getContentAsByteArray();
		if (content.length > 0) {
			logContent(null, "Response Body:",content, response.getContentType(), response.getCharacterEncoding());
		}
	}

	private static void logContent(String requestURI, String prifix, byte[] content, String contentType, String contentEncoding) {
		MediaType mediaType = MediaType.valueOf(contentType);
		boolean visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
		
		if (visible) {
			try {
				String contentString = new String(content, "UTF-8");

				log.info("{} {}", prifix, maskLog(requestURI, contentString));

			} catch (UnsupportedEncodingException e) {
				log.info("[{} bytes content]", content.length);
			}
		} else {
			log.info("[{} bytes content]", content.length);
		}
	}

	private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
		if (request instanceof ContentCachingRequestWrapper) {
			return (ContentCachingRequestWrapper) request;
		} else {
			return new ContentCachingRequestWrapper(request);
		}
	}

	private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
		if (response instanceof ContentCachingResponseWrapper) {
			return (ContentCachingResponseWrapper) response;
		} else {
			return new ContentCachingResponseWrapper(response);
		}
	}
	
	private static String maskLog(String requestURI, String content) {
		
		if(StringUtils.isBlank(content)){
			return content;
		}
		
		if(StringUtils.isBlank(requestURI)){
			return content;
		}
		
		if(maskLogMap == null || maskLogMap.isEmpty()){
			return content;
		}
		
		LogMask logMask = maskLogMap.get(requestURI);
		
		if(logMask == null){
			return content;
		}
		
		try {
			
			//如果匹配，需要将原content的json string 转为 JSONObject，然后 脱敏 相应的 key 对应的 值  
			
			List<String> fields = logMask.getFields();
						
			if(fields == null || fields.isEmpty()){
				return content;
			}
			
			//1.content转 JSONObject
			JSONObject params = JSONObject.parseObject(content);
			
			if(params == null){
				return content;
			}
			
			Map<String,Object> paramsMap = (Map<String,Object>)params;
			
			//2.脱敏map
			String _fieldValue = null;
			
			for (String field : fields) {				
				_fieldValue = params.getString(field);
				if(StringUtils.isBlank(_fieldValue)){
					continue;
				}
				
				paramsMap.put(field, doMasking(_fieldValue));
			}
						
			//3.JSONObject to json string
			return JSON.toJSONString(paramsMap);

		} catch (Exception e) {
			log.error("maskLog", e);
		}
	
		return content;
	}
	

	public static final String separator_mask = "****";
	
	public static String doMasking(String content){
		if(StringUtils.isBlank(content)){
			return content;
		}
		
		int len = content.length();
		
		if(len <= 2){
			return separator_mask;
		}
		
		StringBuilder result = new StringBuilder();
		
		result.append(content.charAt(0));
		result.append(separator_mask);
		result.append(content.charAt(len-1));
		
		return result.toString();
	}
	

	public static void setMaskLogMap(Map<String, LogMask> maskLogMap) {
		RequestAndResponseLoggingFilter.maskLogMap = maskLogMap;
	}
	
	
	
}
