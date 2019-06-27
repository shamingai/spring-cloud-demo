package saas.core.user;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import saas.core.constants.AgentConstants;

import java.io.Serializable;

@Slf4j
public class AgentLocal implements Serializable {

	private static final long serialVersionUID = -8731885206822092316L;
	
	private static ThreadLocal<AgentInfo> agentThreadLocal = new TransmittableThreadLocal<AgentInfo>();
	

    public static AgentInfo get() {
        return agentThreadLocal.get();
    }

    public static void save(AgentInfo agentInfo) {
        agentThreadLocal.set(agentInfo);
    }
    
    public static String getAgentId(){    	

    	AgentInfo appInfo = agentThreadLocal.get();
    	
    	if(appInfo == null){
    		return null;
    	}
    	
    	return StringUtils.trim(appInfo.getAgentId());
    }

    
    public static String getAgentVersion(){
    	AgentInfo agentInfo = agentThreadLocal.get();
    	
    	if(agentInfo == null){
    		return null;
    	}
    	
    	return StringUtils.trim(agentInfo.getAgentVersion()); 
    }
    
    //tenantAppAgentId = wudao
    // getAgentId = app2b-wudao
    //判断 tenantAppAgentId 与 getAgentId 是否匹配
    public static boolean matchingAgent(String tenantAppAgentId){
    	
		if(StringUtils.isBlank(tenantAppAgentId)) {
	    	log.info("******* tenantAppAgentId isBlank ");
			return false;
		}

		//例如 五道app的值为 ： app2b-wudao ,后缀 wudao
		String headerAgentIdSuffix = getAgentIdSuffix();
		
		if(tenantAppAgentId.equalsIgnoreCase(headerAgentIdSuffix)) {
			log.info("******* equalsIgnoreCase=true");
			return true;
		}

		log.info("******* equalsIgnoreCase=false");
    	return false;
    }
    
    //获取 fyi-agent-id 后缀，   app2b-wudao 将返回 wudao
    public static String getAgentIdSuffix(){
    	
    	String result = StringUtils.EMPTY;
    	
    	String agentId = getAgentId();
    	
    	if(StringUtils.isBlank(agentId)) {
			log.info("agentId.isBlank return false");
    		return result;
    	}    	
    	
		//判断是否包含 - ，如果包含 - 说明是 自有app企业	
		if(agentId.indexOf(AgentConstants.AGENT_ID_SPLIT) == -1) {
			log.info("agentId.indexOf == -1 return false");
			return result;
		}
		
		String[] agentIdArray = StringUtils.split(agentId, AgentConstants.AGENT_ID_SPLIT);
		
		if(agentIdArray == null || agentIdArray.length == 0){
			log.info("agentIdArray.length == 0 return false");
			return result;
		}
		
		String appAgentId = agentIdArray[agentIdArray.length-1];
		
		if(StringUtils.isNotBlank(appAgentId)){
			log.info("agentIdArray.length == 0 return true, getAgentIdSuffix=" + appAgentId);
			return appAgentId;
		}
    	
    	return result; 
    }
    
}
