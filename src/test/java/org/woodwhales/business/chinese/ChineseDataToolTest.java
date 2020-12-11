package org.woodwhales.business.chinese;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ChineseDataToolTest {

    /**
     * 排序结果：
     * ChineseDataToolTest.TempData(name=曹操, id=1)
     * ChineseDataToolTest.TempData(name=大乔, id=6)
     * ChineseDataToolTest.TempData(name=关羽, id=9)
     * ChineseDataToolTest.TempData(name=刘备, id=8)
     * ChineseDataToolTest.TempData(name=小乔, id=7)
     * ChineseDataToolTest.TempData(name=张飞, id=2)
     * ChineseDataToolTest.TempData(name=赵云, id=3)
     * ChineseDataToolTest.TempData(name=周瑜, id=4)
     * ChineseDataToolTest.TempData(name=诸葛亮, id=5)
     */
    @Test
    public void test() {
        List<TempData> dataList = new ArrayList<>();
        dataList.add(new TempData("曹操", 1));
        dataList.add(new TempData("大乔", 6));
        dataList.add(new TempData("小乔", 7));
        dataList.add(new TempData("周瑜", 4));
        dataList.add(new TempData("诸葛亮", 5));
        dataList.add(new TempData("张飞", 2));
        dataList.add(new TempData("赵云", 3));
        dataList.add(new TempData("刘备", 8));
        dataList.add(new TempData("关羽", 9));

        List<TempData> list = ChineseDataTool.sortedList(dataList, data -> data.getName());
        list.stream().forEach(System.out::println);
    }

    @Getter
    @ToString
    @AllArgsConstructor
    static class TempData {
        private String name;
        private Integer id;
    }

}