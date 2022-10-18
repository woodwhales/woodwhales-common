package cn.woodwhales.common.example.validation;

import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.common.validation.ValidationTool;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author woodwhales on 2022-10-17 14:06
 */
public class ValidationToolExample {

    public static void main(String[] args) {
        ValidationToolModel validationToolModel = new ValidationToolModel();
        OpResult<Void> opResult = ValidationTool.validate(validationToolModel);
        System.out.println(opResult.getBaseRespResult().getCode());
        System.out.println(opResult.getBaseRespResult().getMessage());
        System.out.println(opResult.isSuccessful());
    }

    @Data
    public static class ValidationToolModel {

        @NotBlank(message = "userName不允许为空")
        private String userName;

        @NotNull(message = "id不允许为空")
        private Long id;

    }

}
