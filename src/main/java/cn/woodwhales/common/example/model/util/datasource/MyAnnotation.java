package cn.woodwhales.common.example.model.util.datasource;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自己定义的属性枚举
 *
 * @author woodwhales on 2021-12-28 18:38
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface MyAnnotation {

    /**
     * 属性对应数据库的字段名
     *
     * @return 属性对应数据库的字段名
     */
    String dbColumn();
}