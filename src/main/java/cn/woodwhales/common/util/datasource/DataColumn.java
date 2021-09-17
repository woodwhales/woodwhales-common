package cn.woodwhales.common.util.datasource;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 数据库表字段属性注解
 * @author woodwhales on 2021-07-24 23:44
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface DataColumn {

    /**
     * The name of the column
     * @return
     */
    String value() default "";

}
