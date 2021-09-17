package cn.woodwhales.common.example.business.chinese;

import cn.woodwhales.common.business.chinese.ChineseDataTool;
import cn.woodwhales.common.example.model.business.chinese.ChineseDataToolTempData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author woodwhales on 2021-07-25 12:07
 * @Description ChineseDataTool 使用示例
 */
public class ChineseDataToolExample {

    public static void main(String[] args) {
        List<ChineseDataToolTempData> dataList = new ArrayList<>();
        dataList.add(new ChineseDataToolTempData("曹操", 1));
        dataList.add(new ChineseDataToolTempData("大乔", 6));
        dataList.add(new ChineseDataToolTempData("小乔", 7));
        dataList.add(new ChineseDataToolTempData("周瑜", 4));
        dataList.add(new ChineseDataToolTempData("诸葛亮", 5));
        dataList.add(new ChineseDataToolTempData("张飞", 2));
        dataList.add(new ChineseDataToolTempData("赵云", 3));
        dataList.add(new ChineseDataToolTempData("刘备", 8));
        dataList.add(new ChineseDataToolTempData("关羽", 9));

        List<ChineseDataToolTempData> list = ChineseDataTool.sortedList(dataList, data -> data.getName());
        list.stream().forEach(System.out::println);
    }
}
