package cn.woodwhales.common.example.model.util.datasource;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 忽略属性映射
 *
 * @author woodwhales on 2021-12-28 18:38
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface DataSourceIgnore {

}
