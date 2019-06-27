package saas.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import saas.pojo.constant.GlobalConstant;

@Data
@ConfigurationProperties(prefix = GlobalConstant.ROOT_PREFIX)
public class SaasProperties {
    private SwaggerProperties swagger = new SwaggerProperties();

}
