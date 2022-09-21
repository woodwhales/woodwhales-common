package cn.woodwhales.common.example.model.business;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author woodwhales on 2022-09-21 12:13
 */
@Getter
@AllArgsConstructor
public enum MyEnum {

    A1(1, "A"),
    A2(2, "A"),
    B1(3, "B"),
    B2(4, "B"),
    C(5, "C"),
    ;
    private Integer code;
    private String type;

}
