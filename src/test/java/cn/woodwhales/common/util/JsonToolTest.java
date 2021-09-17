package cn.woodwhales.common.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class JsonToolTest {

    private static Map<String, String> replaceMap;

    @BeforeAll
    public static void init() {
        replaceMap = new HashMap<>();
        replaceMap.put("False", "false");
        replaceMap.put("True", "true");
    }

    @Test
    public void test2() {
        List<DemoData> demoDataList = JsonTool.parse(getJson2(), new TypeReference<List<DemoData>>() {}, replaceMap);
        demoDataList.stream().forEach(System.out::println);
    }

    @Test
    public void test1() {
        DemoData demoData1 = JsonTool.parse(getJson1(), new TypeReference<DemoData>() {}, replaceMap);
        System.out.println(demoData1);
    }

    private String getJson1() {
        String json1 = "{" +
                "    'id': 'AA'," +
                "    'name': 'BB'," +
                "    'desc': 'CC'," +
                "    'Is_Admin': False" +
                "}";
        return json1;
    }

    private String getJson2() {
        String json2 = "[{\"id\":\"1\",\"name\":\"木鲸鱼\"},{\"id\":\"2\",\"name\":\"woodwhales\",\"Is_admin\": False}]";
        return json2;
    }

    @Data
    public static class DemoData {

        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("desc")
        private String description;

        @JsonProperty("Is_Admin")
        private Boolean isAdmin;

        @JsonProperty("Is_admin")
        private Boolean isAdmin2;
    }
}