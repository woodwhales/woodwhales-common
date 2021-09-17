package cn.woodwhales.common.business.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author woodwhales on 2020-12-13 17:35
 */
class CollectionMathResultTest {

    @Test
    void compute() {
        List<DemoData1> list1 = new ArrayList<>();
        list1.add(new DemoData1("AA", "DemoData1 --> AA"));
        list1.add(new DemoData1("AA", "DemoData1 --> AA"));
        list1.add(new DemoData1("BB", "DemoData1 --> BB"));
        list1.add(new DemoData1("123", "DemoData1 --> 123"));
        list1.add(new DemoData1("D", "DemoData1 --> D"));

        List<DemoData2> list2 = new ArrayList<>();
        list2.add(new DemoData2("CC", "CC"));
        list2.add(new DemoData2("C", "C"));
        list2.add(new DemoData2("AA", "AA"));
        list2.add(new DemoData2("DD", "DD"));
        list2.add(new DemoData2("D", "D"));

        // AA AA BB 123 D
        // AA           D  CC  C  DD

        CollectionMathResult<String, DemoData1, DemoData2> result = CollectionMathResult.compute(list1, DemoData1::getDataNo,
                                                                                                list2, DemoData2::getId);

        // 交集
        Set<String> intersectionKeySet = result.getIntersectionKeySet();
        Set<CollectionFieldComparable<String>> intersectionSet = result.getIntersectionSet();
        print("intersectionKeySet", intersectionKeySet);
        print("intersectionSet", intersectionSet);

        // 并集
        Set<CollectionFieldComparable<String>> unionSet = result.getUnionSet();
        Set<String> unionKeySet = result.getUnionKeySet();
        print("unionKeySet", unionKeySet);
        print("unionSet", unionSet);

        // 反差集
        Set<CollectionFieldComparable<String>> negativeDifferenceSet = result.getNegativeDifferenceSet();
        Set<String> negativeDifferenceKeySet = result.getNegativeDifferenceKeySet();
        List<DemoData2> negativeDifferenceList = result.getNegativeDifferenceList();
        print("negativeDifferenceKeySet", negativeDifferenceKeySet);
        print("negativeDifferenceSet", negativeDifferenceSet);
        print("negativeDifferenceList", negativeDifferenceList);

        list1.add(new DemoData1("EEE", "DemoData1 --> EEE"));

        // 正差集
        Set<CollectionFieldComparable<String>> positiveDifferenceSet = result.getPositiveDifferenceSet();
        Set<String> positiveDifferenceKeySet = result.getPositiveDifferenceKeySet();
        List<DemoData1> positiveDifferenceList = result.getPositiveDifferenceList();
        print("positiveDifferenceKeySet", positiveDifferenceKeySet);
        print("positiveDifferenceSet", positiveDifferenceSet);
        print("positiveDifferenceList", positiveDifferenceList);

        print("unionKeySet", result.getUnionKeySet());
        print("unionSet", result.getUnionSet());

    }

    private void print(String name, Set set) {
        System.out.println("======== " + name + " =======");
        set.stream().forEach(System.out::println);
        System.out.println("======== " + name + " =======");
        System.out.println();
    }

    private void print(String name, List list) {
        System.out.println("======== " + name + " =======");
        list.stream().forEach(System.out::println);
        System.out.println("======== " + name + " =======");
        System.out.println();
    }

    @Data
    @AllArgsConstructor
    class DemoData1 {
        private String dataNo;
        private String dataName;
    }

    @Data
    @AllArgsConstructor
    class DemoData2 {
        private String id;
        private String name;
    }
}