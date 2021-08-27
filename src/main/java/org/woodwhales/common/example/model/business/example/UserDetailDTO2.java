package org.woodwhales.common.example.model.business.example;

import lombok.Data;
import org.woodwhales.common.business.DataTool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author woodwhales on 2021-08-27 15:03
 * @description
 */
@Data
public class UserDetailDTO2 {

    private Integer userId;
    private String userName;
    private List<ReadBookInfoDTO> readBookInfoList;

    public UserDetailDTO2(UserInfoDTO userInfoDTO) {
        this.userId = userInfoDTO.getId();
        this.userName = userInfoDTO.getUserName();
        this.readBookInfoList = new ArrayList<>();
    }

    public static void addReadBookInfo(UserDetailDTO2 userDetailDTO2, List<UserExtraInfoDTO2> userExtraInfoDTO2List) {
        userDetailDTO2.getReadBookInfoList().addAll(DataTool.toList(userExtraInfoDTO2List,
                                                                 userExtraInfoDTO2 -> new ReadBookInfoDTO(userExtraInfoDTO2.getBookName(), userExtraInfoDTO2.getCompleteReadCount())));
    }

    @Data
    public static class ReadBookInfoDTO {
        private String bookName;
        private Integer completeReadCount;

        public ReadBookInfoDTO(String bookName, Integer completeReadCount) {
            this.bookName = bookName;
            this.completeReadCount = completeReadCount;
        }
    }
}
