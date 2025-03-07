package cn.zlf.middleware.db.router.annotation;

import java.lang.annotation.*;

/**
 * @Author: zhonglf
 * @Date: 2025/03/04/22:24
 * @Description:路由注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DBRouter {

    //分库分表字段
    String key() default "";
}
