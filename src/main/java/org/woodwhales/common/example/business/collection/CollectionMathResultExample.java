package org.woodwhales.common.example.business.collection;

import org.woodwhales.common.business.collection.CollectionFieldComparable;
import org.woodwhales.common.business.collection.CollectionMathResult;
import org.woodwhales.common.example.model.business.collection.CollectionMathResultTempData1;
import org.woodwhales.common.example.model.business.collection.CollectionMathResultTempData2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author woodwhales on 2021-07-25 12:12
 * @Description CollectionMathResult 使用示例
 */
public class CollectionMathResultExample {

    public static void main(String[] args) {
        List<CollectionMathResultTempData1> list1 = new ArrayList<>();
        list1.add(new CollectionMathResultTempData1("AA", "CollectionMathResultTempData1 --> AA"));
        list1.add(new CollectionMathResultTempData1("AA", "CollectionMathResultTempData1 --> AA"));
        list1.add(new CollectionMathResultTempData1("BB", "CollectionMathResultTempData1 --> BB"));
        list1.add(new CollectionMathResultTempData1("123", "CollectionMathResultTempData1 --> 123"));
        list1.add(new CollectionMathResultTempData1("D", "CollectionMathResultTempData1 --> D"));

        List<CollectionMathResultTempData2> list2 = new ArrayList<>();
        list2.add(new CollectionMathResultTempData2("CC", "CC"));
        list2.add(new CollectionMathResultTempData2("C", "C"));
        list2.add(new CollectionMathResultTempData2("AA", "AA"));
        list2.add(new CollectionMathResultTempData2("DD", "DD"));
        list2.add(new CollectionMathResultTempData2("D", "D"));

        // AA AA BB 123 D
        // AA           D  CC  C  DD

        CollectionMathResult<String, CollectionMathResultTempData1, CollectionMathResultTempData2> result = CollectionMathResult.compute(list1, CollectionMathResultTempData1::getDataNo,
                list2, CollectionMathResultTempData2::getId);

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
        List<CollectionMathResultTempData2> negativeDifferenceList = result.getNegativeDifferenceList();
        print("negativeDifferenceKeySet", negativeDifferenceKeySet);
        print("negativeDifferenceSet", negativeDifferenceSet);
        print("negativeDifferenceList", negativeDifferenceList);

        list1.add(new CollectionMathResultTempData1("EEE", "CollectionMathResultTempData1 --> EEE"));

        // 正差集
        Set<CollectionFieldComparable<String>> positiveDifferenceSet = result.getPositiveDifferenceSet();
        Set<String> positiveDifferenceKeySet = result.getPositiveDifferenceKeySet();
        List<CollectionMathResultTempData1> positiveDifferenceList = result.getPositiveDifferenceList();
        print("positiveDifferenceKeySet", positiveDifferenceKeySet);
        print("positiveDifferenceSet", positiveDifferenceSet);
        print("positiveDifferenceList", positiveDifferenceList);

        print("unionKeySet", result.getUnionKeySet());
        print("unionSet", result.getUnionSet());
    }

    private static void print(String name, Set set) {
        System.out.println("======== " + name + " =======");
        set.stream().forEach(System.out::println);
        System.out.println("======== " + name + " =======");
        System.out.println();
    }

    private static void print(String name, List list) {
        System.out.println("======== " + name + " =======");
        list.stream().forEach(System.out::println);
        System.out.println("======== " + name + " =======");
        System.out.println();
    }

}
