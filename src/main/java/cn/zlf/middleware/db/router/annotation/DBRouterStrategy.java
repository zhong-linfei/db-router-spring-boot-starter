package cn.zlf.middleware.db.router.annotation;

import java.lang.annotation.*;

/**
 * @Author: zhonglf
 * @Date: 2025/03/06/15:51
 * @Description: 路由策略，分表标记
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DBRouterStrategy {

    boolean splitTable() default false;
}
