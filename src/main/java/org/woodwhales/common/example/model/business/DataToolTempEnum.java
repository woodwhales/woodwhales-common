package org.woodwhales.common.example.model.business;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author woodwhales on 2021-07-25 12:41
 * @Description
 */
@Getter
@AllArgsConstructor
public enum DataToolTempEnum {
    YELLOW(1, "黄色"),
    GREEN(2, "绿色"),
    BLUE(3, "蓝色"),
    ;

    private final Integer code;
    private final String description;
}
