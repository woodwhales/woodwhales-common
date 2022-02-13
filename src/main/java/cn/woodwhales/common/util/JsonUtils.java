package cn.woodwhales.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 通用Json工具类
 *
 * @author woodwhales
 */
public final class JsonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    private final static ObjectMapper OBJECT_MAPPER_INCLUDE_ALWAYS = new ObjectMapper();
    private final static ObjectMapper OBJECT_MAPPER_INCLUDE_NONEMPTY = new ObjectMapper();

    static {
        OBJECT_MAPPER_INCLUDE_ALWAYS.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER_INCLUDE_NONEMPTY.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        OBJECT_MAPPER_INCLUDE_NONEMPTY.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private JsonUtils() {
    }

    /**
     * 将Java对象转成Json，约束规则JsonInclude.Include.NON_EMPTY
     *
     * @param object object 对象
     * @return json 字符串
     */
    public static String toJson(Object object) {
        return toJson(object, JsonInclude.Include.NON_EMPTY);
    }

    /**
     * 将Java对象转成Json，默认约束规则JsonInclude.Include.NON_EMPTY
     *
     * @param object  object 对象
     * @param include JsonInclude.Include
     * @return json字符串
     */
    public static String toJson(Object object, JsonInclude.Include include) {
        if (object == null) {
            return "";
        }
        ObjectMapper objectMapper;
        if (include == null || include == JsonInclude.Include.NON_EMPTY) {
            objectMapper = OBJECT_MAPPER_INCLUDE_NONEMPTY;
        } else if (include == JsonInclude.Include.ALWAYS) {
            objectMapper = OBJECT_MAPPER_INCLUDE_ALWAYS;
        } else {
            objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(include);
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            LOGGER.error("JSON转换异常{}", object, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将Json转成Java对象
     *
     * @param json  json 字符串
     * @param clazz Class对象
     * @param <T>   返回结果类型
     * @return 返回对象
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return OBJECT_MAPPER_INCLUDE_ALWAYS.readValue(json, clazz);
        } catch (IOException e) {
            LOGGER.error("JSON转换异常{},{}", json, clazz, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将Json转成Java对象
     *
     * @param json          json 字符串
     * @param typeReference TypeReference
     * @param <T>           TypeReference泛型中的泛型
     * @return TypeReference泛型中的对象
     */
    public static <T> T toTypeReference(String json, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return OBJECT_MAPPER_INCLUDE_ALWAYS.readValue(json, typeReference);
        } catch (IOException e) {
            LOGGER.error("JSON转换异常{},{}", json, typeReference, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将Json转成Java对象
     *
     * @param json     json 字符串
     * @param javaType JavaType
     * @param <T>      JavaType的泛型
     * @return JavaType的泛型对象
     */
    public static <T> T toJavaType(String json, JavaType javaType) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return OBJECT_MAPPER_INCLUDE_ALWAYS.readValue(json, javaType);
        } catch (IOException e) {
            LOGGER.error("JSON转换异常{},{}", json, javaType, e);
            throw new RuntimeException(e);
        }
    }
}