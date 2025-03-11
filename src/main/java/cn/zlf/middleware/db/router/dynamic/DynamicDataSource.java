package cn.zlf.middleware.db.router.dynamic;

import cn.zlf.middleware.db.router.DBContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Author: zhonglf
 * @Date: 2025/03/07/9:56
 * @Description:动态数据源获取，每当切换数据源，都要从这里面获取
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Value("${mini-db-router.jdbc.datasource.default}")
    private String defaultDataSource;


    @Override
    protected Object determineCurrentLookupKey() {
        if (null == DBContextHolder.getDBKey()) {
            return defaultDataSource;
        } else {
            return "db" + DBContextHolder.getDBKey();
        }
    }
}
