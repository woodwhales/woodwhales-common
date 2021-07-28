package org.woodwhales.common.util.excel;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author woodwhales on 2021-07-28 16:58
 * @description
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface ExcelDateField {

    /**
     * 时间格式：默认 yyyy-MM-dd HH:mm:ss
     * @return
     */
    String value() default "yyyy-MM-dd HH:mm:ss";

}
