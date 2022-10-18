package cn.woodwhales.common.validation;

import cn.woodwhales.common.model.result.OpResult;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 参数校验工具
 * @author woodwhales on 2022-10-17 14:01
 */
public class ValidationTool {

    private static Validator validator;

    static {
        validator = Validation.byProvider(HibernateValidator.class).configure()
                // 快速失败
                .failFast(true)
                .buildValidatorFactory()
                .getValidator();
    }

    public static OpResult<Void> validate(Object obj) {
        Set<ConstraintViolation<Object>> results = validator.validate(obj);
        if (CollectionUtils.isEmpty(results)) {
            return OpResult.success();
        }

        for (ConstraintViolation<Object> result : results) {
            String message = result.getMessage();
            System.out.println("message = " + message);
        }

        return OpResult.error(results.stream().map(ConstraintViolation::getMessage).findFirst().get());
    }

}
