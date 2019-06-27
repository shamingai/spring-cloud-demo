package saas.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class SpringContextHolder implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
    private static ApplicationContext applicationContext;
    private static volatile boolean refreshed;

    public static boolean hasApplicationContext() {
        return (refreshed && applicationContext != null);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static boolean isRefreshed() {
        return refreshed;
    }

    /**
     * 获取对象
     *
     * @return Object 一个以所给名字注册的bean的实例
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 获取类型为requiredType的对象
     * 如果bean不能被类型转换，相应的异常将会被抛出（BeanNotOfRequiredTypeException）
     *
     * @param name         bean注册名
     * @param requiredType 返回对象类型
     * @return Object 返回requiredType类型对象
     */
    @SuppressWarnings("unchecked")
	public static Object getBean(String name, @SuppressWarnings("rawtypes") Class requiredType) throws BeansException {
        return applicationContext.getBean(name, requiredType);
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
     * 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @return boolean
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.isSingleton(name);
    }

    /**
     * @return Class 注册对象的类型
     */
    @SuppressWarnings("rawtypes")
	public static Class getType(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getAliases(name);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        refreshed = true;
    }

    /**
     * 国际化使用
     *
     * @param key
     * @return
     */
    public static String getMessage(String key) {
        return applicationContext.getMessage(key, null, Locale.getDefault());
    }

    /**
     * 获取当前环境
     *
     * @return
     */
    public static String getActiveProfile() {
        return applicationContext.getEnvironment().getActiveProfiles()[0];
    }

    /**
     * 获取当前环境
     *
     * @return
     */
    public static String[] getActiveProfiles() {
        return applicationContext.getEnvironment().getActiveProfiles();
    }
}
