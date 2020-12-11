package org.woodwhales.business.tree;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * @author woodwhales on 2020-12-10
 * @description
 */
public class TreeToolTest {

    private List<Menu> getMenuList() {
        List<Menu> list = Lists.newArrayListWithCapacity(6);

        list.add(new Menu(9, null, "上海", 3));

        list.add(new Menu(1, null, "北京", 1));
        list.add(new Menu(2, 1, "海淀", 1));
        list.add(new Menu(4, 1, "西城", 3));
        list.add(new Menu(3, 1, "朝阳", 2));
        list.add(new Menu(9, 1, "东城", 0));


        list.add(new Menu(5, null, "安徽", 2));
        list.add(new Menu(6, 5, "合肥", 1));
        list.add(new Menu(7, 5, "安庆", 2));
        list.add(new Menu(8, 5, "黄山", 3));

        return list;
    }

    @Test
    public void test() {
        List<TreeNode<Integer, Menu>> tree1 = TreeTool.tree(getMenuList(), new MyTreeNodeGenerator());

        System.out.println(new Gson().toJson(tree1));

        List<Map<String, Object>> tree2 = TreeTool.tree(getMenuList(), new MyTreeNodeGenerator(),
                                                                        TreeNodeAttributeMapper.builder()
                                                                                                .nodeId("key")
                                                                                                .childrenName("child")
                                                                                                .parentId("fatherId")
                                                                                                .sortName("weight")
                                                                                                .build());
        System.out.println(new Gson().toJson(tree2));

        List<TreeNode<Integer, Menu>> tree3 = TreeTool.tree(getMenuList(), new MyTreeNodeGenerator(), true);
        System.out.println(new Gson().toJson(tree3));

        List<Map<String, Object>> tree4 = TreeTool.tree(getMenuList(), new MyTreeNodeGenerator(),
                TreeNodeAttributeMapper.builder()
                        .nodeId("key")
                        .childrenName("child")
                        .parentId("fatherId")
                        .sortName("weight")
                        .dataName("origin")
                        .build(), true);
        System.out.println(new Gson().toJson(tree4));
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
