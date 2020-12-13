package org.woodwhales.business.tree;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * @author woodwhales on 2020-12-10
 * @description
 */
public class TreeToolTest {

    private static List<Menu> testList;

    @BeforeAll
    public static void init() {
        List<Menu> list = Lists.newArrayListWithCapacity(6);

        list.add(new Menu(9, null, "上海", 30));

        list.add(new Menu(1, null, "北京", 10));
        list.add(new Menu(2, 1, "海淀", 1));
        list.add(new Menu(4, 1, "西城", 3));
        list.add(new Menu(3, 1, "朝阳", 2));
        list.add(new Menu(9, 1, "东城", 0));

        list.add(new Menu(5, null, "安徽", 20));
        list.add(new Menu(6, 5, "合肥", 1));
        list.add(new Menu(7, 5, "安庆", 2));
        list.add(new Menu(8, 5, "黄山", 3));

        testList = list;
    }

    @Test
    public void test1() {
        List<TreeNode<Integer, Menu>> tree1 = TreeTool.tree(this.testList, new MyTreeNodeGenerator());
        print(tree1);
    }

    @Test
    public void test2() {
        List<Map<String, Object>> tree2 = TreeTool.tree(this.testList, new MyTreeNodeGenerator(),
                TreeNodeAttributeMapper.<Menu>builder()
                        .nodeId("key")
                        .childrenName("child")
                        .parentId("fatherId")
                        .sortName("weight")
                        .build());
        print(tree2);
    }

    @Test
    public void test3() {
        List<TreeNode<Integer, Menu>> tree3 = TreeTool.tree(this.testList, new MyTreeNodeGenerator(), true);
        print(tree3);
    }

    @Test
    public void test4() {
        List<Map<String, Object>> tree4 = TreeTool.tree(this.testList, new MyTreeNodeGenerator(),
                TreeNodeAttributeMapper.<Menu>builder()
                        .nodeId("key")
                        .nodeName("key")
                        .childrenName("child")
                        .parentId("fatherId")
                        .sortName("weight")
                        .dataName("origin")
                        .build(), true);
        print(tree4);
    }

    @Test
    public void test5() {
        List<Map<String, Object>> tree5 = TreeTool.tree(this.testList, new MyTreeNodeGenerator(),
                TreeNodeAttributeMapper.<Menu>builder()
                        .nodeId("key")
                        .overNodeId(Menu::getCityName)
                        .build(), true);
        print(tree5);
    }

    private void print(Object object) {
        System.out.println("data => " + new Gson().toJson(object));
    }

    static class MyTreeNodeGenerator implements TreeNodeGenerator<Integer, Menu> {
        @Override
        public Integer getId(Menu data) {
            return data.getId();
        }

        @Override
        public Integer getParentId(Menu data) {
            return data.getParentId();
        }

        @Override
        public String getName(Menu data) {
            return data.getCityName();
        }

        @Override
        public boolean isRootNode(Menu data) {
            return data.getParentId() == null;
        }

        @Override
        public int getSort(Menu data) {
            return data.getSort();
        }
    }

    static class Menu {
        private Integer id;
        private Integer parentId;
        private String cityName;
        private Integer sort;

        public Menu(Integer id, Integer parentId, String cityName, Integer sort) {
            this.id = id;
            this.parentId = parentId;
            this.cityName = cityName;
            this.sort = sort;
        }

        public Integer getId() {
            return id;
        }

        public Integer getParentId() {
            return parentId;
        }

        public String getCityName() {
            return cityName;
        }

        public Integer getSort() {
            return sort;
        }
    }

}
