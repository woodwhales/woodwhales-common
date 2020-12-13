package org.woodwhales.business.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.units.qual.K;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author woodwhales on 2020-12-13 17:35
 * @description
 */
class CollectionToolTest {

    @Test
    void mathCollection() {

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

        CollectionMathResult<String, DemoData1, DemoData2> result = CollectionTool.compute(list1, DemoData1::getDataNo, list2, DemoData2::getId);

        // 交集
        Set<String> intersectionKeySet = result.getIntersectionKeySet();
        Set<CollectionFieldComparable<String>> intersectionSet = result.getIntersectionSet();
        print("intersectionKeySet", intersectionKeySet);
        print("intersectionSet", intersectionSet);

        // 并集
        Set<CollectionContainer<String>> unionSet = result.getUnionSet();
        Set<String> unionKeySet = result.getUnionKeySet();
        print("unionKeySet", unionKeySet);
        print("unionSet", unionSet);

        // 反差集
        Set<CollectionContainer<String>> negativeDifferenceSet = result.getNegativeDifferenceSet();
        Set<String> negativeDifferenceKeySet = result.getNegativeDifferenceKeySet();
        print("negativeDifferenceKeySet", negativeDifferenceKeySet);
        print("negativeDifferenceSet", negativeDifferenceSet);

        // 正差集
        Set<CollectionContainer<String>> positiveDifferenceSet = result.getPositiveDifferenceSet();
        Set<String> positiveDifferenceKeySet = result.getPositiveDifferenceKeySet();
        print("positiveDifferenceKeySet", positiveDifferenceKeySet);
        print("positiveDifferenceSet", positiveDifferenceSet);

    }

    private void print(String name, Set set) {
        System.out.println(name + " => " + set);
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