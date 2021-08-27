package org.woodwhales.common.example.model.business.example;

import lombok.Data;

/**
 * @author woodwhales on 2021-08-27 16:12
 * @description
 */
@Data
public class UserExtraInfoDTO2 {

    private Integer userId;
    private String bookName;
    private Integer completeReadCount;

    public UserExtraInfoDTO2(Integer userId, String bookName, Integer completeReadCount) {
        this.userId = userId;
        this.bookName = bookName;
        this.completeReadCount = completeReadCount;
    }
}
