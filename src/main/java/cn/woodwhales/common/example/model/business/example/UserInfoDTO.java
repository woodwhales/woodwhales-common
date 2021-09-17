package cn.woodwhales.common.example.model.business.example;

import lombok.Data;

/**
 * @author woodwhales on 2021-08-27 15:02
 * @description
 */
@Data
public class UserInfoDTO {

    private Integer id;
    private String userName;

    public UserInfoDTO(Integer id, String userName) {
        this.id = id;
        this.userName = userName;
    }
}
