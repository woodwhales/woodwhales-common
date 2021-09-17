package cn.woodwhales.common.example.model.business.example;

import lombok.Data;

/**
 * @author woodwhales on 2021-08-27 15:03
 * @description
 */
@Data
public class UserExtraInfoDTO {

    private Integer userId;
    private Integer readCount;
    private String address;

    public UserExtraInfoDTO(Integer userId, Integer readCount, String address) {
        this.userId = userId;
        this.readCount = readCount;
        this.address = address;
    }
}
