package saas.core.config;

import com.google.common.base.Predicates;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import saas.core.EnvManager;
import saas.core.annotation.RpcApi;
import saas.core.properties.SaasProperties;
import saas.core.properties.SwaggerProperties;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EnableSwagger2
@ComponentScan(basePackages = {
        "saas"
})
public class SwaggerConfig {
    @Resource
    private SaasProperties saasProperties;


    /**
     * swagger 默认配置
     * Rest api
     *
     * @return Docket
     */
    @Bean
    public Docket swaggerRestApi() {
        SwaggerProperties swaggerProperties = saasProperties.getSwagger();

        return getDefaultDocket()
                .apiInfo(new ApiInfoBuilder()
                        .title(swaggerProperties.getTitleRestApi())
                        .description(swaggerProperties.getDescriptionRestApi())
                        .version(swaggerProperties.getVersion())
                        .contact(new Contact(swaggerProperties.getContactName(), "", swaggerProperties.getContactEmail()))
                        .build())
                .groupName(swaggerProperties.getGroupNameRestApi())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(Predicates.not(RequestHandlerSelectors.withClassAnnotation(RpcApi.class)))
                .paths(PathSelectors.any())

                .build()
                .globalOperationParameters(buildTokenParam())
                .enable(swaggerProperties.isEnabled() && swaggerProperties.isEnableDefault())
                //TODO: 异常情况的返回格式
                ;
    }

    /**
     * swagger 默认配置
     * rpc api
     *
     * @return Docket
     */
    @Bean
    public Docket swaggerRpcApi() {
        SwaggerProperties swaggerProperties = saasProperties.getSwagger();

        return getDefaultDocket()
                .apiInfo(new ApiInfoBuilder()
                        .title(swaggerProperties.getTitleRpcApi())
                        .description(swaggerProperties.getDescriptionRpcApi())
                        .version(swaggerProperties.getVersion())
                        .contact(new Contact(swaggerProperties.getContactName(), "", swaggerProperties.getContactEmail()))
                        .build())
                .groupName(swaggerProperties.getGroupNameRpcApi())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.withClassAnnotation(RpcApi.class))
                .paths(PathSelectors.any())

                .build()
                .globalOperationParameters(buildTokenParam())
                .enable(swaggerProperties.isEnabled() && swaggerProperties.isEnableDefault())
                //TODO: 异常情况的返回格式
                ;
    }


    private List<Parameter> buildTokenParam(){

    	String defaultValue = ""; 
    	
    	List<Parameter> pars = new ArrayList<Parameter>();

    	ParameterBuilder tokenPar = new ParameterBuilder();
    	tokenPar.name("Token").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).defaultValue(defaultValue).build();    	
    	pars.add(tokenPar.build());
    	
    	tokenPar = new ParameterBuilder();
    	tokenPar.name("fyi-agent-id").description("客户端ID，例如：app2b").modelRef(new ModelRef("string")).parameterType("header").required(false).defaultValue(defaultValue).build();
    	pars.add(tokenPar.build());

    	tokenPar = new ParameterBuilder();
    	tokenPar.name("fyi-agent-os").description("客户端操作系统，例如：IOS，Android，WinPhone,Windows NT").modelRef(new ModelRef("string")).parameterType("header").required(false).defaultValue(defaultValue).build();
    	pars.add(tokenPar.build());
    	
    	tokenPar = new ParameterBuilder();
    	tokenPar.name("fyi-agent-mid").description("客户端设备id，例如：I-044B1AC84CB044909C94874ADB34E994").modelRef(new ModelRef("string")).parameterType("header").required(false).defaultValue(defaultValue).build();
    	pars.add(tokenPar.build());
    	
    	tokenPar = new ParameterBuilder();
    	tokenPar.name("fyi-agent-version").description("客户端版本，例如：6.0.7").modelRef(new ModelRef("string")).parameterType("header").required(false).defaultValue(defaultValue).build();
    	pars.add(tokenPar.build());
    	
    	return pars;
    }
    
    /**
     * 获取默认docket：
     * swagger2、生产禁用、指定basePath、禁用默认ResponseMessage
     *
     * @return docket
     */
    public Docket getDefaultDocket() {
        SwaggerProperties swaggerProperties = saasProperties.getSwagger();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerProperties.isEnabled() && !EnvManager.isProduction())
                .protocols(getProtocols())
                .forCodeGeneration(true)
                .useDefaultResponseMessages(false);

        return buildPathProvider(docket);
    }

    /**
     * 根据配置设置basePath
     *
     * @param docket 要构建的docket
     * @return docket
     */
    public Docket buildPathProvider(Docket docket) {
        SwaggerProperties swaggerProperties = saasProperties.getSwagger();

        String basePath = swaggerProperties.getBasePath();
        if (StringUtils.isNotBlank(basePath)) {
            docket.pathMapping(basePath);
        }

        return docket;
    }

    /**
     * 允许的协议 https/http
     *
     * @return https、http
     */
    @SuppressWarnings("serial")
	public static Set<String> getProtocols() {
        return new HashSet<String>() {
            {
                add("HTTP");
                //add("HTTPS");
            }
        };
    }

    /**
     * 异常ResponseMessage列表 (400/500)
     * 需要将Model: ErrorResultWrapper 添加到 ApiResponse 的 response 中,否则报错
     *
     * @return responseMessage list
     */
    protected List<ResponseMessage> getErrorResponseMessageList() {
        List<ResponseMessage> result = new ArrayList<>();
        result.add(new ResponseMessageBuilder().code(400).message("请求无效").responseModel(new ModelRef("ErrorResultWrapper")).build());
        result.add(new ResponseMessageBuilder().code(500).message("服务器错误").responseModel(new ModelRef("ErrorResultWrapper")).build());

        return result;
    }

    /**
     * TODO:全局异常返回格式
     * 根据属性 SwaggerContants.PROPERTY_KEY_SWAGGER_PROJECT_ENABLE_GLOBAL_ERROR_RESPONSE 的配置 处理全局ResponseMessage
     * true(默认为true) 则为GET/POST/DELETE/PUT/PATCH 添加 getErrorResponseMessageList() 的全局处理
     *
     * @param docket
     * @return Docket
     */
    /*
    protected Docket buildGlobalResponseMessage(Docket docket) {

        boolean enableGlobalErrorResponse = true;

        if (enableGlobalErrorResponse) {
            docket.globalResponseMessage(RequestMethod."GET", getErrorResponseMessageList())
                    .globalResponseMessage(RequestMethod.POST, getErrorResponseMessageList())
                    .globalResponseMessage(RequestMethod.DELETE, getErrorResponseMessageList())
                    .globalResponseMessage(RequestMethod.PUT, getErrorResponseMessageList())
                    .globalResponseMessage(RequestMethod.PATCH, getErrorResponseMessageList());
        }

        return docket;
    }
    */
}
