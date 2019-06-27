package saas.core.spring;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import saas.core.constants.AgentConstants;
import saas.core.constants.TokenConstants;
import saas.core.user.*;
import saas.util.IpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInfoHandlerInterceptor implements HandlerInterceptor {
	
	public static final String ERROR_REQUEST = "/error";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		String URI = request.getRequestURI();
		
		if(ERROR_REQUEST.equals(URI)){
			return true;
		}

		//log.info("====>{} {}?{}", request.getMethod(), URI, StringUtils.defaultString(request.getQueryString()));
		
		parseAgentInfo(request);
		
		parseToken(request);
		
		return true;
	}

	
	protected AgentInfo parseAgentInfo(HttpServletRequest request){
		AgentInfo agentInfo = null;
		
		String agentId = request.getHeader(AgentConstants.HEADER_AGENT_ID);
		
		if (StringUtils.isNotBlank(agentId)) {
			if(agentInfo == null){
				agentInfo = new AgentInfo();
			}
			agentInfo.setAgentId(agentId);
		}
		
		String agentOS = request.getHeader(AgentConstants.HEADER_AGENT_OS);
		
		if (StringUtils.isNotBlank(agentOS)) {
			if(agentInfo == null){
				agentInfo = new AgentInfo();			
			}
			agentInfo.setAgentOs(agentOS);
		}
		
		String agentMid = request.getHeader(AgentConstants.HEADER_AGENT_MID);
		
		if (StringUtils.isNotBlank(agentMid)) {
			if(agentInfo == null){
				agentInfo = new AgentInfo();			
			}
			agentInfo.setAgentMid(agentMid);
		}
		
		String agentVersion = request.getHeader(AgentConstants.HEADER_AGENT_VERSION);
		
		if (StringUtils.isNotBlank(agentVersion)) {
			if(agentInfo == null){
				agentInfo = new AgentInfo();			
			}
			agentInfo.setAgentVersion(agentVersion);
		}
		
		//String agentIp = request.getHeader(AgentConstants.HEADER_AGENT_IP);
		String agentIp = IpUtil.getRequestIp(request);
		
		if (StringUtils.isNotBlank(agentIp)) {
			if(agentInfo == null){
				agentInfo = new AgentInfo();			
			}
			agentInfo.setAgentIp(agentIp);
		}

		if(agentInfo != null){
			// 解析Agent，将客户端Agent信息放入 TransmittableThreadLocal 中
			AgentLocal.save(agentInfo);
		}
		
		return agentInfo;
		
	}
	
	
	protected void parseToken(HttpServletRequest request){
		
		String token = request.getHeader(TokenConstants.TOKEN_HEADER_KEY);

		if (StringUtils.isBlank(token)) {			
			return;
		}
		
		TokenInfo tokenInfo = TokenUtil.decodeToken(token);
		
		if (tokenInfo != null) {
			// 解析token，将用户信息放入 TransmittableThreadLocal 中
			SessionLocal.save(tokenInfo);
		}
				
	}
		

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o,
			ModelAndView modelAndView) throws Exception {

		// log.info("========postHandle==========="+o.toString());

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e)
			throws Exception {

		// log.info("========afterCompletion==========="+o.toString());

	}

}
