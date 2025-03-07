package cn.zlf.middleware.db.router.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: zhonglf
 * @Date: 2025/03/06/15:55
 * @Description:数据源配置解析
 */
@Configuration
public class DataSourceAutoConfig {

    //分库全局属性
    private static final String TAG_GLOBAL = "global";

    //连接池属性
    private static final String TAG_POOL = "pool";

    //数据源配置组
    private final Map<String, Map<String, Object>> dataSourceMap = new ConcurrentHashMap<String, Map<String, Object>>();

    //默认数据源配置
    private Map<String, Object> defaultDataSourceConfig;

    //分库数量
    private int dbCount;

    //分表数量
    private int tbCount;

    //路由字段
    private String routerKey;

//TODO


}
