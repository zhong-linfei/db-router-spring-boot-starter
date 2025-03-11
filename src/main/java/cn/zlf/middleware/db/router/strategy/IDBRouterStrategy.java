package cn.zlf.middleware.db.router.strategy;

/**
 * @Author: zhonglf
 * @Date: 2025/03/07/15:28
 * @Description: 路由策略
 */
public interface IDBRouterStrategy {
    //路由计算
    void doRouter(String dbKeyAttr);

    //手动设置分库路由
    void setDBKey(int dbIdx);

    //手动设置分表路由
    void setTBKey(int tbIdx);

    //获取分库数
    int dbCount();

    //获取分表数
    int tbCount();

    //清除路由
    void clear();
}
