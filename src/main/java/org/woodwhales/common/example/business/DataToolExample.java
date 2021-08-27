package org.woodwhales.common.example.business;

import com.google.gson.Gson;
import org.woodwhales.common.business.DataTool;
import org.woodwhales.common.business.DeduplicateResult;
import org.woodwhales.common.example.model.business.*;
import org.woodwhales.common.example.model.business.example.UserDTO;
import org.woodwhales.common.example.model.business.example.UserDetailDTO;
import org.woodwhales.common.example.model.business.example.UserExtraInfoDTO;

import java.util.*;

/**
 * @author woodwhales on 2021-07-25 12:34
 * @Description DataTool 使用示例
 */
public class DataToolExample {

    public static void main(String[] args) {
//        deduplicate();
//        enumMap1();
//        enumMap2();
//        getDataFromList();
//        groupingBy();
//        handleMap();
//        testMapToList();
//        testToList();
//        toList();
//        toMap1();
//        toMap2();
        testGetListFromBaseList();
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
        DataToolTempData data1 = new DataToolTempData(1, "张三", "描述-张三");
        DataToolTempData data2 = new DataToolTempData(2, "李四", "描述-李四");
        DataToolTempData data3 = new DataToolTempData(3, "王五", "描述-王五");
        DataToolTempData data4 = new DataToolTempData(null, "赵六", "描述-赵六");
        DataToolTempData data5 = new DataToolTempData(3, "朱七", "描述-朱七");
        DataToolTempData data6 = new DataToolTempData(4, "宋八", "描述-宋八");

        list.add(data1);
        list.add(data2);
        list.add(data3);
        list.add(data4);
        list.add(data5);
        list.add(data6);

        DeduplicateResult<Integer, DataToolTempData> deduplicateResult1 = DataTool.deduplicate(list,
                data -> Objects.nonNull(data.getId()),
                DataToolTempData::getId, true);

        List<DataToolTempData> deduplicatedList = deduplicateResult1.getDeduplicatedList();
        List<DataToolTempData> repetitiveList = deduplicateResult1.getRepetitiveList();
        List<DataToolTempData> invalidList = deduplicateResult1.getInvalidList();
        List<Integer> deduplicatedKeyList = deduplicateResult1.getDeduplicatedKeyList();

        System.out.println("deduplicatedList");
        deduplicatedList.stream().forEach(System.out::println);

        System.out.println("repetitiveList");
        repetitiveList.stream().forEach(System.out::println);

        System.out.println("invalidList");
        invalidList.stream().forEach(System.out::println);

        System.out.println("deduplicatedKeyList");
        System.out.println(deduplicatedKeyList);

        // 无效数据
        assertEquals(1, invalidList.size());
        assertEquals(data4, invalidList.get(0));

        // 已去重数据
        assertEquals(4, deduplicatedList.size());

        // 重复数据
        assertEquals(1, repetitiveList.size());
        assertEquals(data5, repetitiveList.get(0));

        DeduplicateResult<Integer, DataToolTempData> deduplicateResult2 = DataTool.deduplicate(list,
                data -> Objects.nonNull(data.getId()),
                DataToolTempData::getId, false);
        List<DataToolTempData> deduplicatedList2 = deduplicateResult2.getDeduplicatedList();
        List<DataToolTempData> repetitiveList2 = deduplicateResult2.getRepetitiveList();
        // 重复数据
        assertEquals(4, deduplicatedList2.size());
        assertEquals(data3, repetitiveList2.get(0));

        DeduplicateResult<Integer, DataToolTempData> deduplicateResult3 = DataTool.deduplicate(list, DataToolTempData::getId);
        List<DataToolTempData> deduplicatedList1 = deduplicateResult3.getDeduplicatedList();
        assertEquals(5, deduplicatedList1.size());
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

    public static void testGetListFromBaseList() {
        List<UserDTO> userDTOList = new ArrayList<>();
        userDTOList.add(new UserDTO(1, "张三"));
        userDTOList.add(new UserDTO(2, "李四"));
        userDTOList.add(new UserDTO(3, "王五"));
        userDTOList.add(new UserDTO(4, "赵六"));
        userDTOList.add(new UserDTO(5, "宋七"));

        List<UserExtraInfoDTO> userExtraInfoDTOList = new ArrayList<>();
        userExtraInfoDTOList.add(new UserExtraInfoDTO(2, 30, "北京"));
        userExtraInfoDTOList.add(new UserExtraInfoDTO(4, 40, "上海"));
        userExtraInfoDTOList.add(new UserExtraInfoDTO(5, 50, "杭州"));

        List<UserDetailDTO> result = DataTool.getListFromBaseList(userDTOList, UserDTO::getId,
                                                                            userExtraInfoDTOList, UserExtraInfoDTO::getUserId,
                                                                            (userDTO, userExtraInfoDTO) -> new UserDetailDTO(userDTO,
                                                                                                                             userExtraInfoDTO));
        result.stream().forEach(System.out::println);
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
        boolean b = objectsAreEqual(expected, actual);
        if(!b) {
            throw new RuntimeException("\nexpected = " + expected + ", actual = " + actual);
        }
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
