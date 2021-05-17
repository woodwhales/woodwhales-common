package org.woodwhales.business;


import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DataToolTest {

    @Test
    public void enumMap2() {
        Map<String, DemoEnum> map = DataTool.enumMap(DemoEnum.class);
        assertEquals(3, map.size());
        printMap(map);
    }

    @Test
    public void enumMap1() {
        Map<Integer, DemoEnum> map = DataTool.enumMap(DemoEnum.class, DemoEnum::getCode);
        assertEquals(3, map.size());
        printMap(map);

        boolean containsKey = DataTool.enumContainsKey(1, DemoEnum.class, DemoEnum::getCode);
        assertEquals(true, containsKey);

        DemoEnum demoEnum = DataTool.enumGetValue(2, DemoEnum.class, DemoEnum::getCode);
        assertEquals(DemoEnum.GREEN, demoEnum);

        DemoEnum demoEnum2 = DataTool.enumGetValue(4, DemoEnum.class, DemoEnum::getCode);
        assertNull(demoEnum2);
    }

    enum DemoEnum {
        YELLOW(1, "黄色"),
        GREEN(2, "绿色"),
        BLUE(3, "蓝色"),
        ;

        private final Integer code;
        private final String description;

        DemoEnum(Integer code, String description) {
            this.code = code;
            this.description = description;
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return "DemoEnum{" +
                    "code=" + code +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    @Test
    public void toMap2() {
        List<DemoData> list = new ArrayList<>();
        list.add(new DemoData(1, "张三", "描述-张三"));
        DemoData demoData1 = new DemoData(2, "李四", "描述-李四");
        list.add(demoData1);
        DemoData demoData2 = new DemoData(2, "李四2", "描述-李四2");
        list.add(demoData2);
        list.add(new DemoData(3, "王五", "描述-王五"));
        list.add(new DemoData(4, "宋八", "描述-宋八"));

        Map<Integer, DemoData> map1 = DataTool.toMapForSaveOld(list, DemoData::getId);

        assertEquals(list.size() - 1, map1.size());
        assertEquals(demoData1, map1.get(2));
        printMap(map1);

        System.out.println();

        Map<Integer, DemoData> map2 = DataTool.toMapForSaveNew(list, DemoData::getId);

        assertEquals(list.size() - 1, map2.size());
        assertEquals(demoData2, map2.get(2));
        printMap(map2);
    }

    @Test
    public void toMap1() {
        List<DemoData> list = new ArrayList<>();
        list.add(new DemoData(1, "张三", "描述-张三"));
        list.add(new DemoData(2, "李四", "描述-李四"));
        list.add(new DemoData(3, "王五", "描述-王五"));
        list.add(new DemoData(4, "宋八", "描述-宋八"));

        Map<Integer, DemoData> integerDemoDataMap = DataTool.toMap(list, DemoData::getId);

        assertEquals(list.size(), integerDemoDataMap.size());
        printMap(integerDemoDataMap);
    }

    @Test
    public void toList() {
        List<DemoData> list = new ArrayList<>();
        list.add(new DemoData(1, "张三", "描述-张三"));
        list.add(new DemoData(2, "李四", "描述-李四"));
        list.add(new DemoData(3, "王五", "描述-王五"));
        list.add(new DemoData(4, "宋八", "描述-宋八"));
        List<String> resultList = DataTool.toList(list, DemoData::getName);
        assertEquals(list.get(0).getName(), resultList.get(0));
        assertEquals(list.get(2).getName(), resultList.get(2));
    }

    private void printMap(Map map) {
        Map<Object, Object> map1 = (Map<Object, Object>) map;
        map1.entrySet().forEach(entry -> {
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(String.format("key = %s, value = %s", key, value));
        });
    }

    @Test
    public void groupingBy() {
        List<DemoData> list = new ArrayList<>();
        list.add(new DemoData(1, "张三", "描述-张三"));
        list.add(new DemoData(2, "李四", "描述-李四"));
        list.add(new DemoData(2, "李四2", "描述-李四2"));
        list.add(new DemoData(3, "王五", "描述-王五"));
        list.add(new DemoData(4, "宋八", "描述-宋八"));

        Map<Integer, List<DemoData>> map = DataTool.groupingBy(list, DemoData::getId);
        assertEquals(list.size() - 1, map.size());
        assertEquals(2, map.get(2).size());
        printMap(map);
    }

    @Test
    public void deduplicate() {
        List<DemoData> list = new ArrayList<>();
        list.add(new DemoData(1, "张三", "描述-张三"));
        list.add(new DemoData(2, "李四", "描述-李四"));
        DemoData demoData3 = new DemoData(3, "王五", "描述-王五");
        list.add(demoData3);
        DemoData demoData4 = new DemoData(null, "赵六", "描述-赵六");
        list.add(demoData4);
        list.add(new DemoData(3, "朱七", "描述-朱七"));
        list.add(new DemoData(4, "宋八", "描述-宋八"));

        DeduplicateResult<DemoData> deduplicateResult = DataTool.deduplicate(list, new DeduplicateInterface<Integer, DemoData>() {
            @Override
            public boolean isValid(DemoData data) {
                return Objects.nonNull(data.getId());
            }

            @Override
            public Integer getDeduplicatedKey(DemoData data) {
                return data.getId();
            }
        });

        List<DemoData> deduplicatedList = deduplicateResult.getDeduplicatedList();
        List<DemoData> repetitiveList = deduplicateResult.getRepetitiveList();
        List<DemoData> invalidList = deduplicateResult.getInvalidList();

        // 无效数据
        assertEquals(1, invalidList.size());
        assertEquals(demoData4, invalidList.get(0));

        // 已去重数据
        assertEquals(4, deduplicatedList.size());

        // 重复数据
        assertEquals(1, repetitiveList.size());
        assertEquals(demoData3, repetitiveList.get(0));
    }

    @Test
    public void getDataFromList() {
        DemoDataDTO demoDataDTO = new DemoDataDTO(3, "上海");

        List<DemoData> list = new ArrayList<>();
        list.add(new DemoData(1, "北京", "北京"));
        list.add(new DemoData(2, "南京", "南京"));
        list.add(new DemoData(3, "上海", "上海"));

        DemoData result = DataTool.getDataFromList(demoDataDTO, DemoDataDTO::getId, list, DemoData::getId);
        System.out.println("result = " + result);
        assertEquals(list.get(2), result);
    }

    @Test
    public void handleMap() {
        Map<Integer, HandleMapDTO> map = new HashMap<>();
        map.put(1, new HandleMapDTO(1, new DemoDataDTO(1, "AA")));
        map.put(2, new HandleMapDTO(2, new DemoDataDTO(2, "BB")));
        map.put(2, new HandleMapDTO(3, new DemoDataDTO(3, "CC")));
        System.out.println("new Gson().toJson(map) = " + new Gson().toJson(map));
        DataTool.handleMap(map, (k, v) -> {
            if("AA".equals(k)) {
                new DemoDataDTO(4, "DD");
            };
        });
        System.out.println("new Gson().toJson(map) = " + new Gson().toJson(map));
    }

    class HandleMapDTO {
        private Integer id;
        private DemoDataDTO demoDataDTO;

        public HandleMapDTO(Integer id, DemoDataDTO demoDataDTO) {
            this.id = id;
            this.demoDataDTO = demoDataDTO;
        }
    }

    class DemoDataDTO {
        private Integer id;
        private String nameForVO;

        public DemoDataDTO(Integer id, String nameForVO) {
            this.id = id;
            this.nameForVO = nameForVO;
        }

        public Integer getId() {
            return id;
        }

        public String getNameForVO() {
            return nameForVO;
        }

        @Override
        public String toString() {
            return "DemoDataDTO{" +
                    "id=" + id +
                    ", nameForVO='" + nameForVO + '\'' +
                    '}';
        }
    }

    class DemoData {

        private Integer id;
        private String name;
        private String description;

        public DemoData(Integer id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return "DemoData{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    @Test
    public void testMapToList() {
        Map<String, DemoDataDTO> map = new HashMap<>();
        map.put("A", new DemoDataDTO(1, "AA"));
        map.put("B", new DemoDataDTO(2, "BB"));
        map.put("C", new DemoDataDTO(3, "CC"));
        List<String> keyList = DataTool.mapToList(map, (key, value) -> key);
        List<String> valueList = DataTool.mapToList(map, (key, value) -> value.toString());
        System.out.println("keyList = " + keyList);
        System.out.println("valueList = " + valueList);

        List<String> filteredList = DataTool.mapToList(map, (key, value) -> key.equals("A"), (key, value) -> value.toString());
        System.out.println("filteredList = " + filteredList);

        List<String> valueList2 = DataTool.mapValueToList(map, DemoDataDTO::getNameForVO);
        System.out.println("valueList2 = " + valueList2);
    }

}