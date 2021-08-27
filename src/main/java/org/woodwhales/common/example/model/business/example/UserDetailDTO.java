package org.woodwhales.common.example.model.business.example;

import lombok.Data;

/**
 * @author woodwhales on 2021-08-27 15:03
 * @description
 */
@Data
public class UserDetailDTO {

    private Integer userId;
    private String userName;
    private Integer readCount;
    private String address;

    public UserDetailDTO(UserInfoDTO userInfoDTO, UserExtraInfoDTO userExtraInfoDTO) {
        this.userId = userInfoDTO.getId();
        this.userName = userInfoDTO.getUserName();
        this.readCount = userExtraInfoDTO.getReadCount();
        this.address = userExtraInfoDTO.getAddress();
    }
}
