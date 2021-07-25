package org.woodwhales.common.example.business;

import com.google.gson.Gson;
import org.woodwhales.common.business.DataTool;
import org.woodwhales.common.business.DeduplicateInterface;
import org.woodwhales.common.business.DeduplicateResult;
import org.woodwhales.common.example.model.business.*;

import java.util.*;

/**
 * @author woodwhales on 2021-07-25 12:34
 * @Description DataTool 使用示例
 */
public class DataToolExample {

    public static void main(String[] args) {
        deduplicate();
        enumMap1();
        enumMap2();
        getDataFromList();
        groupingBy();
        handleMap();
        testMapToList();
        testToList();
        toList();
        toMap1();
        toMap2();
    }

    public static void enumMap1() {
        Map<Integer, DataToolTempEnum> map = DataTool.enumMap(DataToolTempEnum.class, DataToolTempEnum::getCode);
        assertEquals(3, map.size());
        printMap(map);

        boolean containsKey = DataTool.enumContainsKey(1, DataToolTempEnum.class, DataToolTempEnum::getCode);
        assertEquals(true, containsKey);

        DataToolTempEnum DataToolTempEnum = DataTool.enumGetValue(2, DataToolTempEnum.class, org.woodwhales.common.example.model.business.DataToolTempEnum::getCode);
        assertEquals(org.woodwhales.common.example.model.business.DataToolTempEnum.YELLOW, DataToolTempEnum);

        DataToolTempEnum DataToolTempEnum2 = DataTool.enumGetValue(4, DataToolTempEnum.class, org.woodwhales.common.example.model.business.DataToolTempEnum::getCode);
        assertNull(DataToolTempEnum2);
    }

    public static void enumMap2() {
        Map<String, DataToolTempEnum> map = DataTool.enumMap(DataToolTempEnum.class);
        assertEquals(3, map.size());
        printMap(map);
    }

    public static void toMap2() {
        List<DataToolTempData> list = new ArrayList<>();
        list.add(new DataToolTempData(1, "张三", "描述-张三"));
        DataToolTempData DataToolTempData1 = new DataToolTempData(2, "李四", "描述-李四");
        list.add(DataToolTempData1);
        DataToolTempData DataToolTempData2 = new DataToolTempData(2, "李四2", "描述-李四2");
        list.add(DataToolTempData2);
        list.add(new DataToolTempData(3, "王五", "描述-王五"));
        list.add(new DataToolTempData(4, "宋八", "描述-宋八"));

        Map<Integer, DataToolTempData> map1 = DataTool.toMapForSaveOld(list, DataToolTempData::getId);

        assertEquals(list.size() - 1, map1.size());
        assertEquals(DataToolTempData1, map1.get(2));
        printMap(map1);

        System.out.println();

        Map<Integer, DataToolTempData> map2 = DataTool.toMapForSaveNew(list, DataToolTempData::getId);

        assertEquals(list.size() - 1, map2.size());
        assertEquals(DataToolTempData2, map2.get(2));
        printMap(map2);
    }

    public static void toMap1() {
        List<DataToolTempData> list = new ArrayList<>();
        list.add(new DataToolTempData(1, "张三", "描述-张三"));
        list.add(new DataToolTempData(2, "李四", "描述-李四"));
        list.add(new DataToolTempData(3, "王五", "描述-王五"));
        list.add(new DataToolTempData(4, "宋八", "描述-宋八"));

        Map<Integer, DataToolTempData> integerDataToolTempDataMap = DataTool.toMap(list, DataToolTempData::getId);

        assertEquals(list.size(), integerDataToolTempDataMap.size());
        printMap(integerDataToolTempDataMap);
    }

    public static void toList() {
        List<DataToolTempData> list = new ArrayList<>();
        list.add(new DataToolTempData(1, "张三", "描述-张三"));
        list.add(new DataToolTempData(2, "李四", "描述-李四"));
        list.add(new DataToolTempData(3, "王五", "描述-王五"));
        list.add(new DataToolTempData(4, "宋八", "描述-宋八"));
        List<String> resultList = DataTool.toList(list, DataToolTempData::getName);
        assertEquals(list.get(0).getName(), resultList.get(0));
        assertEquals(list.get(2).getName(), resultList.get(2));
    }

    public static void testToList() {
        HashMap<Integer, DataToolTempMapData1> map = new HashMap<>();
        map.put(1, new DataToolTempMapData1(1, new DataToolTempMapData2(1, "map1")));
        map.put(2, new DataToolTempMapData1(2, new DataToolTempMapData2(2, "map2")));
        map.put(3, new DataToolTempMapData1(3, new DataToolTempMapData2(3, "map3")));

        List<DataToolTempListData3> list = new ArrayList<>();
        list.add(new DataToolTempListData3(1, "A1"));
        list.add(new DataToolTempListData3(2, "A2"));
        list.add(new DataToolTempListData3(3, "A3"));
        list.add(new DataToolTempListData3(4, "A4"));

        List<DataToolTempListResult> DataToolTempListResults = DataTool.toListWithMap(list, map, DataToolTempListData3::getKey, (DataToolTempListData3, DataToolTempMapData1) -> {
            Integer key = DataToolTempListData3.getKey();
            DataToolTempMapData2 DataToolTempMapData2 = DataToolTempMapData1.getDataToolTempMapData2();
            return new DataToolTempListResult(key, DataToolTempMapData2);
        });

        DataToolTempListResults.forEach(System.out::println);

        System.out.println("=========");

        List<DataToolTempListResult> DataToolTempListResults2 = DataTool.toListWithMap(list, map, DataToolTempListData3::getKey, (DataToolTempListData3, DataToolTempMapData1) -> {
            Integer key = DataToolTempListData3.getKey();
            DataToolTempMapData2 DataToolTempMapData2 = DataToolTempMapData1.getDataToolTempMapData2();
            return new DataToolTempListResult(key, DataToolTempMapData2);
        }, DataToolTempListData3 -> {
            Integer key = DataToolTempListData3.getKey();
            return new DataToolTempListResult(key, null);
        });

        DataToolTempListResults2.forEach(System.out::println);
    }

    public static void groupingBy() {
        List<DataToolTempData> list = new ArrayList<>();
        list.add(new DataToolTempData(1, "张三", "描述-张三"));
        list.add(new DataToolTempData(2, "李四", "描述-李四"));
        list.add(new DataToolTempData(2, "李四2", "描述-李四2"));
        list.add(new DataToolTempData(3, "王五", "描述-王五"));
        list.add(new DataToolTempData(4, "宋八", "描述-宋八"));

        Map<Integer, List<DataToolTempData>> map = DataTool.groupingBy(list, DataToolTempData::getId);
        assertEquals(list.size() - 1, map.size());
        assertEquals(2, map.get(2).size());
        printMap(map);
    }

    public static void deduplicate() {
        List<DataToolTempData> list = new ArrayList<>();
        list.add(new DataToolTempData(1, "张三", "描述-张三"));
        list.add(new DataToolTempData(2, "李四", "描述-李四"));
        DataToolTempData demoData3 = new DataToolTempData(3, "王五", "描述-王五");
        list.add(demoData3);
        DataToolTempData demoData4 = new DataToolTempData(null, "赵六", "描述-赵六");
        list.add(demoData4);
        list.add(new DataToolTempData(3, "朱七", "描述-朱七"));
        list.add(new DataToolTempData(4, "宋八", "描述-宋八"));

        DeduplicateResult<DataToolTempData> deduplicateResult = DataTool.deduplicate(list, new DeduplicateInterface<Integer, DataToolTempData>() {
            @Override
            public boolean isValid(DataToolTempData data) {
                return Objects.nonNull(data.getId());
            }

            @Override
            public Integer getDeduplicatedKey(DataToolTempData data) {
                return data.getId();
            }
        });

        List<DataToolTempData> deduplicatedList = deduplicateResult.getDeduplicatedList();
        List<DataToolTempData> repetitiveList = deduplicateResult.getRepetitiveList();
        List<DataToolTempData> invalidList = deduplicateResult.getInvalidList();

        // 无效数据
        assertEquals(1, invalidList.size());
        assertEquals(demoData4, invalidList.get(0));

        // 已去重数据
        assertEquals(4, deduplicatedList.size());

        // 重复数据
        assertEquals(1, repetitiveList.size());
        assertEquals(demoData3, repetitiveList.get(0));
    }

    public static void getDataFromList() {
        DataToolTempDataDTO demoDataDTO = new DataToolTempDataDTO(3, "上海");

        List<DataToolTempData> list = new ArrayList<>();
        list.add(new DataToolTempData(1, "北京", "北京"));
        list.add(new DataToolTempData(2, "南京", "南京"));
        list.add(new DataToolTempData(3, "上海", "上海"));

        DataToolTempData result = DataTool.getDataFromList(demoDataDTO, DataToolTempDataDTO::getId, list, DataToolTempData::getId);
        System.out.println("result = " + result);
        assertEquals(list.get(2), result);
    }

    public static void handleMap() {
        Map<Integer, DataToolTempHandleMapDTO> map = new HashMap<>();
        map.put(1, new DataToolTempHandleMapDTO(1, new DataToolTempDataDTO(1, "AA")));
        map.put(2, new DataToolTempHandleMapDTO(2, new DataToolTempDataDTO(2, "BB")));
        map.put(2, new DataToolTempHandleMapDTO(3, new DataToolTempDataDTO(3, "CC")));
        System.out.println("new Gson().toJson(map) = " + new Gson().toJson(map));
        DataTool.handleMap(map, (k, v) -> {
            if("AA".equals(k)) {
                new DataToolTempDataDTO(4, "DD");
            };
        });
        System.out.println("new Gson().toJson(map) = " + new Gson().toJson(map));
    }

    public static void testMapToList() {
        Map<String, DataToolTempDataDTO> map = new HashMap<>();
        map.put("A", new DataToolTempDataDTO(1, "AA"));
        map.put("B", new DataToolTempDataDTO(2, "BB"));
        map.put("C", new DataToolTempDataDTO(3, "CC"));
        List<String> keyList = DataTool.mapToList(map, (key, value) -> key);
        List<String> valueList = DataTool.mapToList(map, (key, value) -> value.toString());
        System.out.println("keyList = " + keyList);
        System.out.println("valueList = " + valueList);

        List<String> filteredList = DataTool.mapToList(map, (key, value) -> key.equals("A"), (key, value) -> value.toString());
        System.out.println("filteredList = " + filteredList);

        List<String> valueList2 = DataTool.mapValueToList(map, DataToolTempDataDTO::getNameForVO);
        System.out.println("valueList2 = " + valueList2);
    }

    private static void printMap(Map map) {
        Map<Object, Object> map1 = (Map<Object, Object>) map;
        map1.entrySet().forEach(entry -> {
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(String.format("key = %s, value = %s", key, value));
        });
    }

    private static void assertEquals(Object expected, Object actual) {
        objectsAreEqual(expected, actual);
    }

    private static boolean objectsAreEqual(Object obj1, Object obj2) {
        if (obj1 == null) {
            return (obj2 == null);
        }
        return obj1.equals(obj2);
    }

    private static void assertNull(Object object) {
        if(object == null) {
            return;
        }
        throw new RuntimeException(object + "校验为空失败");
    }
}
