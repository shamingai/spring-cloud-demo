package saas.core.properties;

import lombok.Data;

@Data
public class SwaggerProperties {
    /**
     * 是否启用swagger (profile != pro 同时 enabled = true 时 启用)
     */
    private boolean enabled = true;

    /**
     * 是否启用默认swagger配置
     */
    private boolean enableDefault = true;

    /**
     * rest 分组名
     */
    private String groupNameRestApi = "rest";

    /**
     * rpc 分组名
     */
    private String groupNameRpcApi = "rpc";

    /**
     * 基础路径
     */
    private String basePath;

    /**
     * rest 标题
     */
    private String titleRestApi = "Web/App 调用 API";

    /**
     * Rpc 标题
     */
    private String titleRpcApi = "微服务调用 RPC API";

    /**
     * Rest 描述
     */
    private String descriptionRestApi = "";

    /**
     * Rpc 描述
     */
    private String descriptionRpcApi = "";

    /**
     * 版本
     */
    private String version = "1.0.0";

    private String contactName = "";
    private String contactEmail = "";
}
