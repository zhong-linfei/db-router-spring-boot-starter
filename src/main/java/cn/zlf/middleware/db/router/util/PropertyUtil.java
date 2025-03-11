package cn.zlf.middleware.db.router.util;

import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: zhonglf
 * @Date: 2025/03/07/15:30
 * @Description: 属性工具类
 */

public class PropertyUtil {

    // 用于存储当前 Spring Boot 版本，默认为 1
    private static int springBootVersion = 1;

    /**
     * 静态代码块，在类加载时执行，用于检测当前 Spring Boot 版本。
     * 通过尝试加载 Spring Boot 1.x 中的 RelaxedPropertyResolver 类来判断版本。
     * 如果加载失败，说明当前使用的是 Spring Boot 2.x 版本，将 springBootVersion 设置为 2。
     */
    
    static {
        try {
            Class.forName("org.springframework.boot.bind.RelaxedPropertyResolver");
        } catch (ClassNotFoundException e) {
            springBootVersion = 2;
        }
    }

    /**
     * Spring Boot 1.x 与 Spring Boot 2.x 的属性处理兼容方法，使用 Java 反射机制。
     * 根据检测到的 Spring Boot 版本，调用相应的属性解析方法。
     *
     * @param environment  环境上下文对象，用于获取属性信息
     * @param prefix       属性键的前缀部分
     * @param targetClass  结果的目标类类型
     * @param <T>          泛型类型，与 targetClass 参数对应
     * @return             返回解析后的属性对象，类型为 T
     */
    @SuppressWarnings("unchecked")
    public static <T> T handle(final Environment environment, final String prefix, final Class<T> targetClass) {
        // 根据 springBootVersion 的值选择不同的处理方法
        switch (springBootVersion) {
            case 1:
                // 若为 Spring Boot 1.x 版本，调用 v1 方法进行属性解析
                return (T) v1(environment, prefix);
            default:
                // 若为 Spring Boot 2.x 版本，调用 v2 方法进行属性解析
                return (T) v2(environment, prefix, targetClass);
        }
    }

    /**
     * 处理 Spring Boot 1.x 版本的属性解析方法。
     * 使用 Java 反射机制调用 Spring Boot 1.x 中的 RelaxedPropertyResolver 类进行属性解析。
     *
     * @param environment  环境上下文对象，用于获取属性信息
     * @param prefix       属性键的前缀部分
     * @return             返回解析后的属性对象
     */
    private static Object v1(final Environment environment, final String prefix) {
        try {
            // 获取 Spring Boot 1.x 中的 RelaxedPropertyResolver 类
            Class<?> resolverClass = Class.forName("org.springframework.boot.bind.RelaxedPropertyResolver");
            // 获取 RelaxedPropertyResolver 类的构造函数，该构造函数接受一个 PropertyResolver 类型的参数
            Constructor<?> resolverConstructor = resolverClass.getDeclaredConstructor(PropertyResolver.class);
            // 获取 RelaxedPropertyResolver 类的 getSubProperties 方法，该方法接受一个 String 类型的参数
            Method getSubPropertiesMethod = resolverClass.getDeclaredMethod("getSubProperties", String.class);
            // 通过构造函数创建 RelaxedPropertyResolver 类的实例
            Object resolverObject = resolverConstructor.newInstance(environment);
            // 处理前缀，确保前缀以 "." 结尾
            String prefixParam = prefix.endsWith(".") ? prefix : prefix + ".";
            // 调用 getSubProperties 方法进行属性解析
            return getSubPropertiesMethod.invoke(resolverObject, prefixParam);
        } catch (final ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
                       | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            // 若发生异常，将异常包装成 RuntimeException 抛出
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * 处理 Spring Boot 2.x 版本的属性解析方法。
     * 使用 Java 反射机制调用 Spring Boot 2.x 中的 Binder 类进行属性绑定和解析。
     *
     * @param environment  环境上下文对象，用于获取属性信息
     * @param prefix       属性键的前缀部分
     * @param targetClass  结果的目标类类型
     * @return             返回解析后的属性对象
     */
    private static Object v2(final Environment environment, final String prefix, final Class<?> targetClass) {
        try {
            // 获取 Spring Boot 2.x 中的 Binder 类
            Class<?> binderClass = Class.forName("org.springframework.boot.context.properties.bind.Binder");
            // 获取 Binder 类的 get 方法，该方法接受一个 Environment 类型的参数
            Method getMethod = binderClass.getDeclaredMethod("get", Environment.class);
            // 获取 Binder 类的 bind 方法，该方法接受一个 String 类型和一个 Class 类型的参数
            Method bindMethod = binderClass.getDeclaredMethod("bind", String.class, Class.class);
            // 调用 get 方法获取 Binder 类的实例
            Object binderObject = getMethod.invoke(null, environment);
            // 处理前缀，若前缀以 "." 结尾，则去掉最后一个字符
            String prefixParam = prefix.endsWith(".") ? prefix.substring(0, prefix.length() - 1) : prefix;
            // 调用 bind 方法进行属性绑定
            Object bindResultObject = bindMethod.invoke(binderObject, prefixParam, targetClass);
            // 获取绑定结果对象的 get 方法
            Method resultGetMethod = bindResultObject.getClass().getDeclaredMethod("get");
            // 调用 get 方法获取最终的属性对象
            return resultGetMethod.invoke(bindResultObject);
        }
        catch (final ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException
                     | IllegalArgumentException | InvocationTargetException ex) {
            // 若发生异常，将异常包装成 RuntimeException 抛出
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}