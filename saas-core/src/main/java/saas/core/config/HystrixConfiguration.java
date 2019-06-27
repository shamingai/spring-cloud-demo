package saas.core.config;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HystrixConfiguration {

	@Bean(name = "hystrixRegistrationBean")
	public ServletRegistrationBean<HystrixMetricsStreamServlet> servletRegistrationBean() {
		ServletRegistrationBean<HystrixMetricsStreamServlet> registration = new ServletRegistrationBean<HystrixMetricsStreamServlet>(
				new HystrixMetricsStreamServlet(), "/hystrix.stream");
		registration.setName("hystrixServlet");
		registration.setLoadOnStartup(1);
		return registration;
	}

	@Bean(name = "hystrixForTurbineRegistrationBean")
	public ServletRegistrationBean<HystrixMetricsStreamServlet> servletTurbineRegistrationBean() {
		ServletRegistrationBean<HystrixMetricsStreamServlet> registration = new ServletRegistrationBean<HystrixMetricsStreamServlet>(
				new HystrixMetricsStreamServlet(), "/actuator/hystrix.stream");
		registration.setName("hystrixForTurbineServlet");
		registration.setLoadOnStartup(1);
		return registration;
	}

}