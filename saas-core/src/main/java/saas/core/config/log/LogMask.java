package saas.core.config.log;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LogMask implements Serializable {

	private static final long serialVersionUID = -7501095795443912058L;
	
	private String uri;
	
	private List<String> fields;
     
}
