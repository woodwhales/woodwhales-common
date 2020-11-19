package org.woodwhales.business;


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
}