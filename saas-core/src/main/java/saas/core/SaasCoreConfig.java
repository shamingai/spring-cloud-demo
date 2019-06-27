package saas.core;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import saas.core.properties.SaasProperties;

@Configuration
@EnableConfigurationProperties(SaasProperties.class)
public class SaasCoreConfig {
}
