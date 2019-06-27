package saas.core.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import saas.core.config.log.LogMask;
import saas.core.config.log.LogMaskConfig;
import saas.core.config.log.RequestAndResponseLoggingFilter;

import java.util.List;
import java.util.Map;

@Slf4j
public class RequestLoggingConfig {
	

	@Autowired
    LogMaskConfig logMaskConfig;
	
	
	@Bean
    public RequestAndResponseLoggingFilter logFilter() {
		RequestAndResponseLoggingFilter filter = new RequestAndResponseLoggingFilter();
		
		
		if(logMaskConfig == null){
			log.info("logMaskConfig is null");
			return filter;	
		}
		
		List<String> apiFields = logMaskConfig.getApifields();
				
		if(apiFields == null || apiFields.isEmpty()){
			log.info("logMaskConfig.getApiFields is null");
			return filter;	
		}
		
		for (String config : apiFields) {
			log.info("  -  "+config);
		}

		Map<String,LogMask> maskLogMap = parseApiAndFieldsConfig(apiFields);
				
		if(maskLogMap.isEmpty()){
			log.info("maskLogMap.isEmpty");
			return filter;	
		}

		RequestAndResponseLoggingFilter.setMaskLogMap(maskLogMap);
		
        return filter;
    }
	
	public static final String separator_field = ",";
	public static final String separator_api = "@";
	
	private Map<String,LogMask> parseApiAndFieldsConfig(List<String> apiFields){

		Map<String,LogMask> result = Maps.newHashMap();
		
		if(apiFields == null || apiFields.isEmpty()){
			log.info("apiFields.isEmpty() is null");
			return result;	
		}
		
		LogMask _logMask = null;
		
		String[] paramArray = null;
		String[] fieldsArray = null;
		List<String> _fields = null;
		
		String _orgiField = null;
		
		for (String _apiFields : apiFields) {			
			if(StringUtils.isBlank(_apiFields)){
				//配置错误，内容为空
				continue;
			}
			
			if(_apiFields.indexOf(separator_api) == -1){
				//配置错误，需要使用 @ 分隔 api和field
				continue;
			}
			
			paramArray = StringUtils.split(_apiFields, separator_api);
			
			if(paramArray == null || paramArray.length <= 1){
				//配置错误
				continue;
			}
			
			_logMask = new LogMask();
			_logMask.setUri(paramArray[0]);
			
			_orgiField = paramArray[1];
			
			_fields = Lists.newArrayList();
			
			fieldsArray = StringUtils.split(_orgiField, separator_field);
			
			for (int i = 0; i < fieldsArray.length; i++) {
				_fields.add(fieldsArray[i]);
			}
			
			_logMask.setFields(_fields);
			
			result.put(paramArray[0], _logMask);
			
		}
		
		return result;
	}
	
}
