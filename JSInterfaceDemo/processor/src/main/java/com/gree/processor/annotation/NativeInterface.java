package com.gree.processor.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lzh
 * @mail 560182@gree.com.cn
 * @time 5/15/21 8:45 AM
 * @detail 这是一个注解 用于标记某个文件下原生接口
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NativeInterface {
    /**
     * js文件的名称
     * @return
     */
    String jsName() default "";
}
