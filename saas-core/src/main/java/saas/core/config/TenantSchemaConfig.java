package saas.core.config;

import org.springframework.context.annotation.Bean;
import saas.core.mybatis.TenantSchemaInterceptor;

public class TenantSchemaConfig {

	@Bean
	public TenantSchemaInterceptor sqlStatsInterceptor() {
		return new TenantSchemaInterceptor();
	}

}
