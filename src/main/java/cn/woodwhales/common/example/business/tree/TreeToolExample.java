package cn.woodwhales.common.example.business.tree;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import cn.woodwhales.common.business.tree.TreeNode;
import cn.woodwhales.common.business.tree.TreeNodeAttributeMapper;
import cn.woodwhales.common.business.tree.TreeTool;
import cn.woodwhales.common.example.model.business.tree.MyTreeNodeGenerator;
import cn.woodwhales.common.example.model.business.tree.TreeToolTempMenu;

import java.util.List;
import java.util.Map;

/**
 * @author woodwhales on 2021-07-25 12:25
 * TreeTool 使用示例
 */
public class TreeToolExample {

    private static List<TreeToolTempMenu> testList;

    public static void init() {
        List<TreeToolTempMenu> list = Lists.newArrayListWithCapacity(6);

        list.add(new TreeToolTempMenu(9, null, "上海", 30));

        list.add(new TreeToolTempMenu(1, null, "北京", 10));
        list.add(new TreeToolTempMenu(2, 1, "海淀", 1));
        list.add(new TreeToolTempMenu(4, 1, "西城", 3));
        list.add(new TreeToolTempMenu(3, 1, "朝阳", 2));
        list.add(new TreeToolTempMenu(9, 1, "东城", 0));

        list.add(new TreeToolTempMenu(5, null, "安徽", 20));
        list.add(new TreeToolTempMenu(6, 5, "合肥", 1));
        list.add(new TreeToolTempMenu(7, 5, "安庆", 2));
        list.add(new TreeToolTempMenu(8, 5, "黄山", 3));

        testList = list;
    }

    public static void main(String[] args) {

        test1();

        test2();

        test3();

        test4();

        test5();
    }

    public static void test1() {
        init();
        List<TreeNode<Integer, TreeToolTempMenu>> tree1 = TreeTool.tree(TreeToolExample.testList, new MyTreeNodeGenerator());
        print(tree1);
    }

    public static void test2() {
        init();
        List<Map<String, Object>> tree2 = TreeTool.tree(TreeToolExample.testList, new MyTreeNodeGenerator(),
                TreeNodeAttributeMapper.<TreeToolTempMenu>builder()
                        .nodeId("key")
                        .childrenName("child")
                        .parentId("fatherId")
                        .sortName("weight")
                        .build());
        print(tree2);
    }

    public static void test3() {
        init();
        List<TreeNode<Integer, TreeToolTempMenu>> tree3 = TreeTool.tree(TreeToolExample.testList, new MyTreeNodeGenerator(), true);
        print(tree3);
    }

    public static void test4() {
        init();
        List<Map<String, Object>> tree4 = TreeTool.tree(TreeToolExample.testList, new MyTreeNodeGenerator(),
                TreeNodeAttributeMapper.<TreeToolTempMenu>builder()
                        .nodeId("key")
                        .nodeName("key")
                        .childrenName("child")
                        .parentId("fatherId")
                        .sortName("weight")
                        .dataName("origin")
                        .build(), true);
        print(tree4);
    }

    public static void test5() {
        init();
        List<Map<String, Object>> tree5 = TreeTool.tree(TreeToolExample.testList, new MyTreeNodeGenerator(),
                TreeNodeAttributeMapper.<TreeToolTempMenu>builder()
                        .nodeId("key")
                        .overNodeId(TreeToolTempMenu::getCityName)
                        .build(), true);
        print(tree5);
    }

    private static void print(Object object) {
        System.out.println("data => " + new Gson().toJson(object));
    }

}
