package cn.woodwhales.common.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * 通过lambda表达式获取属性名称
 * @author woodwhales on 2022-09-16 22:29
 */
public class LambdaTool {

    public static <T, F> String getColumnName(SFunction<T, F> function) {
        LambdaMeta meta = LambdaUtils.extract(function);
        String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
        Class<?> instantiatedClass = meta.getInstantiatedClass();
        Class<T> clazz = (Class<T>) instantiatedClass;
        List<Field> allFieldsList = FieldUtils.getAllFieldsList(clazz);

        Field field = allFieldsList.stream()
                .filter( f -> f.getName().equals(fieldName))
                .findAny()
                .orElse(null);

        String columnName = null;
        if(Objects.nonNull(field)) {
            if(field.isAnnotationPresent(TableField.class)) {
                columnName = field.getAnnotation(TableField.class).value();
            } else if(field.isAnnotationPresent(TableId.class)) {
                columnName = field.getAnnotation(TableId.class).value();
            } else {
                columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
            }
        }
        return columnName;
    }

}
