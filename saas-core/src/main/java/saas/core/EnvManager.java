package saas.core;

import saas.core.spring.SpringContextHolder;
import saas.pojo.constant.GlobalConstant;

import java.util.Arrays;

public class EnvManager {
    /**
     * 是否生产环境
     *
     * @return
     */
    public static boolean isProduction() {
        String[] profiles = SpringContextHolder.getActiveProfiles();
        return Arrays.stream(profiles).anyMatch(p -> GlobalConstant.DEV_PROFILE.equalsIgnoreCase(p));
    }
}
