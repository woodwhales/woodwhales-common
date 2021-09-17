package cn.woodwhales.common.example.model.business.tree;

import cn.woodwhales.common.business.tree.TreeNodeGenerator;

/**
 * @author woodwhales on 2021-07-25 12:26
 */
public class MyTreeNodeGenerator implements TreeNodeGenerator<Integer, TreeToolTempMenu> {
    @Override
    public Integer getId(TreeToolTempMenu data) {
        return data.getId();
    }

    @Override
    public Integer getParentId(TreeToolTempMenu data) {
        return data.getParentId();
    }

    @Override
    public String getName(TreeToolTempMenu data) {
        return data.getCityName();
    }

    @Override
    public boolean isRootNode(TreeToolTempMenu data) {
        return data.getParentId() == null;
    }

    @Override
    public int getSort(TreeToolTempMenu data) {
        return data.getSort();
    }
}

