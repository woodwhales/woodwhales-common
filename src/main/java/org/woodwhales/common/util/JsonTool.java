package org.woodwhales.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * @author woodwhales
 * @date 2020-12-26 00:21
 */
public class JsonTool {

    private static final Logger log = LoggerFactory.getLogger(JsonTool.class);

    /**
     * 解析 jsonStr 为 对象
     * json字符串中 key 映射对象中属性，可以使用 @JsonProperty 指定
     *
     * @param jsonStr json字符串
     * @param typeReference 对象类型
     * @param replaceMap 字符替换集合
     * @param <T> 对象类型
     * @return
     */
    public static <T> T parse(String jsonStr, TypeReference<T> typeReference, Map<String, String> replaceMap) {
        if(isBlank(jsonStr)) {
            log.warn("this jsonStr is blank");
            return null;
        }

        if(MapUtils.isNotEmpty(replaceMap)) {
            for (Map.Entry<String, String> replaceEntry : replaceMap.entrySet()) {
                if(contains(jsonStr, replaceEntry.getKey())) {
                    jsonStr = replace(jsonStr, replaceEntry.getKey(), replaceEntry.getValue());
                }
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        T target = null;
        try {
            target = mapper.readValue(jsonStr, typeReference);
        } catch (JsonProcessingException e) {
            log.error("parse fail, jsonStr = {}, errorMsg = {}", jsonStr, e.getMessage(), e);
        }

        return target;
    }

    /**
     * 解析 jsonStr 为 对象
     * json字符串中 key 映射对象中属性，可以使用 @JsonProperty 指定
     *
     * @param jsonStr json字符串
     * @param typeReference TypeReference对象
     * @param <T> 对象类型
     * @return
     */
    public static <T> T parse(String jsonStr, TypeReference<T> typeReference) {
        return parse(jsonStr, typeReference, null);
    }

}
