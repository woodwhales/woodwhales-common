package cn.woodwhales.common.example.util;

import cn.woodwhales.common.example.model.business.DataToolTempData;
import cn.woodwhales.common.util.JsonTool;

/**
 * @author woodwhales on 2022-09-21 12:39
 */
public class JsonToolExample {

    public static void main(String[] args) {
        DataToolTempData data = new DataToolTempData(null , "A", "A desc");
        String jsonStr = JsonTool.toJSONString(data);
        System.out.println("jsonStr = " + jsonStr);
    }

}
