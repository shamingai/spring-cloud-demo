package saas.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import saas.core.spring.UserInfoHandlerInterceptor;

public class  SessionLocalConfig implements WebMvcConfigurer {
    
	@Autowired
    private UserInfoHandlerInterceptor userInfoHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInfoHandlerInterceptor);
    }
    
}
