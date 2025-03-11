package cn.zlf.middleware.db.router;

/**
 * @Author: zhonglf
 * @Date: 2025/03/10/20:51
 * @Description:数据源基础配置
 */
public class DBRouterBase {

    private String tbIdx;
    public String getTbIdx() {
        return DBContextHolder.getTBKey();
    }
}
