package cn.woodwhales.common.example.business.tree;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import cn.woodwhales.common.business.tree.TreeNode;
import cn.woodwhales.common.business.tree.TreeNodeAttributeMapper;
import cn.woodwhales.common.business.tree.TreeTool;
import cn.woodwhales.common.example.model.business.tree.MyTreeNodeGenerator;
import cn.woodwhales.common.example.model.business.tree.TreeToolTempMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author woodwhales on 2021-07-25 12:25
 * TreeTool 使用示例
 */
public class TreeToolExample {

    private static List<TreeToolTempMenu> testList;

    public static void main(String[] args) {

        test1();

        test2();

        test3();

        test4();

        test5();

        test6();

    }

    public static void init() {
        List<TreeToolTempMenu> list = Lists.newArrayListWithCapacity(6);

        list.add(new TreeToolTempMenu(9, null, "上海", 30, "上海很美")
                .setExtraInfo1("有东方明珠")
                .setExtraInfo2("有上海滩")
        );

        list.add(new TreeToolTempMenu(1, null, "北京", 10, "北京是首都"));
        list.add(new TreeToolTempMenu(2, 1, "海淀", 1, "有很多大学"));
        list.add(new TreeToolTempMenu(4, 1, "西城", 3, "北京的西边"));
        list.add(new TreeToolTempMenu(3, 1, "朝阳", 2, "CBD聚集地")
                .setExtraInfo1("有三里屯")
                .setExtraInfo2("有朝阳大悦城")
        );
        list.add(new TreeToolTempMenu(9, 1, "东城", 0, "北京的东边"));

        list.add(new TreeToolTempMenu(5, null, "安徽", 20, "有很多山")
                .setExtraInfo1("有茶叶")
                .setExtraInfo2("有山有水")
        );
        list.add(new TreeToolTempMenu(6, 5, "合肥", 1, "安徽省会")
                .setExtraInfo1("有中科大")
                .setExtraInfo2("有步行街")
        );
        list.add(new TreeToolTempMenu(7, 5, "安庆", 2, "黄梅戏很有名")
                .setExtraInfo1("不知道有啥")
                .setExtraInfo2("有火车站")
        );
        list.add(new TreeToolTempMenu(8, 5, "黄山", 3, "有迎客松")
                .setExtraInfo1("山上有石头")
                .setExtraInfo2("山上有树木")
        );

        testList = list;
    }

    /**
     * 简单树华数据
     *
     * MyTreeNodeGenerator 定义了从 TreeToolTempMenu 中如何获取如下信息：
     *
     * 节点的唯一标识、
     * 节点的名称、
     * 子节点的名称
     * 根节点的判断标准
     * 排序值
     *
     */
    public static void test1() {
        init();
        List<TreeNode<Integer, TreeToolTempMenu>> tree = TreeTool.tree(TreeToolExample.testList, new MyTreeNodeGenerator());
        print(tree);
    }

    /**
     * 通过 TreeNodeAttributeMapper 更改掉默认的节点名称等，并且不展示排序值
     */
    public static void test2() {
        init();
        List<Map<String, Object>> tree = TreeTool.tree(TreeToolExample.testList, new MyTreeNodeGenerator(),
                TreeNodeAttributeMapper.<TreeToolTempMenu>builder()
                        .nodeIdName("key")
                        .childrenName("child")
                        .parentId("fatherId")
                        .sortName(null)
                        .build());
        print(tree);
    }

    public static void test3() {
        init();
        List<Map<String, Object>> tree = TreeTool.tree(TreeToolExample.testList, new MyTreeNodeGenerator(),
                TreeNodeAttributeMapper.<TreeToolTempMenu>builder()
                        .nodeIdName("key")
                        .nodeName("key")
                        .childrenName("child")
                        .parentId("fatherId")
                        .sortName("weight")
                        .dataName("origin")
                        .build(), true);
        print(tree);
    }

    public static void test4() {
        init();
        List<Map<String, Object>> tree = TreeTool.tree(TreeToolExample.testList, new MyTreeNodeGenerator(),
                TreeNodeAttributeMapper.<TreeToolTempMenu>builder()
                        .nodeIdName("key")
                        .overNodeId(TreeToolTempMenu::getCityName)
                        .build(), true);
        print(tree);
    }

    public static void test5() {
        init();
        List<Map<String, Object>> tree = TreeTool.tree(TreeToolExample.testList, new MyTreeNodeGenerator(),
                TreeNodeAttributeMapper.<TreeToolTempMenu>builder()
                                                        .nodeIdName("key")
                                                        .overNodeId(TreeToolTempMenu::getCityName)
                                                        .build(),
                                                        TreeToolTempMenu::getMemo, false);
        print(tree);
    }

    public static void test6() {
        init();

        List<TreeNodeAttributeMapper.ExtraMapping<TreeToolTempMenu>> extraMappingList = new ArrayList<>();
        extraMappingList.add(new TreeNodeAttributeMapper.ExtraMapping<TreeToolTempMenu>("备注1", TreeToolTempMenu::getExtraInfo1));
        extraMappingList.add(new TreeNodeAttributeMapper.ExtraMapping<TreeToolTempMenu>("备注2", TreeToolTempMenu::getExtraInfo2));

        List<Map<String, Object>> test = TreeTool.tree(TreeToolExample.testList, new MyTreeNodeGenerator(),
                                        TreeNodeAttributeMapper.<TreeToolTempMenu>builder()
                                                                .extraMappingList(extraMappingList)
                                                                .build());
        print(test);
    }

    private static void print(Object object) {
        System.out.println("data => " + new Gson().toJson(object));
    }

}
