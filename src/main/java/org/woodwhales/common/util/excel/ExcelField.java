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
public @interface ExcelField {

    /**
     * excel列字段
     * @return
     */
    String value() default "";

}
