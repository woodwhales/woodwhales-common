package org.woodwhales.common.validation;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 校验枚举值的处理器
 * @author woodwhales
 */
public class EnumValidatorValidator implements ConstraintValidator<EnumValidator, Object> {

	private final static Logger log = LoggerFactory.getLogger(EnumValidatorValidator.class);

	private List<Object> values = new ArrayList<>();
	
	@Override
	public void initialize(EnumValidator enumValidator) {
		Class<? extends Enum>[] classArray = enumValidator.target();
		if(classArray.length == 0) {
			return;
		}

		try {
			addValue(classArray, enumValidator.methodName());
		} catch (Exception exception) {
			log.error("handle exception process happening exception! {}", exception);
		}
	}
	
	private void addValue(Class<? extends Enum>[] classArray, String methodString) throws Exception {
		// 获取@EnumValidator注解类上的value配置，即枚举的Class类对象
		for (Class<? extends Enum> clz : classArray) {
			if(clz.isEnum()) {
				// 获取当前枚举类的所有实例对象
				Object[] objects = clz.getEnumConstants();
				// 调用指定的获取校验值对应的方法，获取Method对象
	            Method method = clz.getMethod(methodString);
	            if (Objects.isNull(method)) {
	                throw new RuntimeException(String.format("枚举对象%s缺少名为%s的方法", clz.getName(), methodString));
	            }
	            Object value;
	            for (Object obj : objects) {
	            	// 依次执行枚举实例的 "getCode()"方法，将其值存入值域列表中
	                value = method.invoke(obj);
	                values.add(value);
	            }
			}
		}
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (value instanceof String) {
            String valueStr = (String)value;
            return StringUtils.isEmpty(valueStr) || values.contains(value);
        }
		
        return Objects.isNull(value) || values.contains(value);
	}

}
