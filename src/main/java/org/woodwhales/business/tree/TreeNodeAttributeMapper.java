package org.woodwhales.business.tree;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author woodwhales on 2020-12-11
 * @description
 */
public class TreeNodeAttributeMapper {

    /**
     * 当前节点 id 的别名
     */
    private String nodeId = "id";

    /**
     * 当前节点名称变量的别名
     */
    private String nodeName = "name";

    /**
     * 当前节点的父节点变量的别名
     */
    private String parentId = "parentId";

    /**
     * 子节点变量的别名
     */
    private String childrenName = "children";

    /**
     * 源数据变量的别名
     */
    private String dataName = "data";

    /**
     * 当前节点的排序变量的别名
     */
    private String sortName = "sort";

    private TreeNodeAttributeMapper() {
    }

    public static TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder builder() {
        return new TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder();
    }

    public static class TreeNodeAttributeMapperBuilder {
        private String nodeId;
        private String nodeName;
        private String parentId;
        private String childrenName;
        private String dataName;
        private String sortName;

        TreeNodeAttributeMapperBuilder() {}

        public TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder nodeId(String nodeId) {
            this.nodeId = nodeId;
            return this;
        }

        public TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder nodeName(String nodeName) {
            this.nodeName = nodeName;
            return this;
        }

        public TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder parentId(String parentId) {
            this.parentId = parentId;
            return this;
        }

        public TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder childrenName(String childrenName) {
            this.childrenName = childrenName;
            return this;
        }

        public TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder dataName(String dataName) {
            this.dataName = dataName;
            return this;
        }

        public TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder sortName(String sortName) {
            this.sortName = sortName;
            return this;
        }

        public TreeNodeAttributeMapper build() {
            TreeNodeAttributeMapper treeNodeAttributeMapper = new TreeNodeAttributeMapper();
            treeNodeAttributeMapper.setNodeId(this.nodeId);
            treeNodeAttributeMapper.setNodeName(this.nodeName);
            treeNodeAttributeMapper.setParentId(this.parentId);
            treeNodeAttributeMapper.setChildrenName(this.childrenName);
            treeNodeAttributeMapper.setDataName(this.dataName);
            treeNodeAttributeMapper.setSortName(this.sortName);
            return treeNodeAttributeMapper;
        }
    }

    public TreeNodeAttributeMapper setNodeId(String nodeId) {
        if(isNotBlank(nodeId)) {
            this.nodeId = nodeId;
        }
        return this;
    }

    public TreeNodeAttributeMapper setNodeName(String nodeName) {
        if(isNotBlank(nodeName)) {
            this.nodeName = nodeName;
        }
        return this;
    }

    public TreeNodeAttributeMapper setParentId(String parentId) {
        if(isNotBlank(parentId)) {
            this.parentId = parentId;
        }
        return this;
    }

    public TreeNodeAttributeMapper setChildrenName(String childrenName) {
        if(isNotBlank(childrenName)) {
            this.childrenName = childrenName;
        }
        return this;
    }

    public TreeNodeAttributeMapper setDataName(String dataName) {
        if(isNotBlank(dataName)) {
            this.dataName = dataName;
        }
        return this;
    }

    public TreeNodeAttributeMapper setSortName(String sortName) {
        if(isNotBlank(sortName)) {
            this.sortName = sortName;
        }
        return this;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getParentId() {
        return parentId;
    }

    public String getChildrenName() {
        return childrenName;
    }

    public String getDataName() {
        return dataName;
    }

    public String getSortName() {
        return sortName;
    }
}
