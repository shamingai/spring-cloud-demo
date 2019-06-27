package saas.core.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import saas.core.config.json.DateTimeSerializer;

import java.util.Date;
import java.util.List;

public class AliFastJsonConfig implements WebMvcConfigurer {


	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		//MessageObjectMapper.buidMvcMessageConverter(converters);
		
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        com.alibaba.fastjson.support.config.FastJsonConfig config = new com.alibaba.fastjson.support.config.FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.WriteNullListAsEmpty, // List类型字段为null时输出[]而非null
                SerializerFeature.WriteMapNullValue, // 显示空字段
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullStringAsEmpty, // 字符串类型字段为null时间输出""而非null
                SerializerFeature.WriteNullBooleanAsFalse, // Boolean类型字段为null时输出false而null
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.PrettyFormat); // 美化json输出，否则会作为整行输出
        
        //converter.setDateFormat("yyyy-MM-dd HH:mm:ss"); // 时间格式
        converter.setFastJsonConfig(config);
        converters.add(converter);
        
        SerializeConfig.getGlobalInstance().put(Date.class, new DateTimeSerializer());
        
	}

}
