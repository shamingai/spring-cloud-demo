package saas.core.config.log;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "masklog")
public class LogMaskConfig {

    private List<String> apifields = new ArrayList<>();

	public List<String> getApifields() {
		return apifields;
	}

	public void setApifields(List<String> apifields) {
		this.apifields = apifields;
	}
    
 
    
}
