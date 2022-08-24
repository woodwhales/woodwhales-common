package cn.woodwhales.common.business.tree;

import java.util.List;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author woodwhales on 2020-12-11
 */
public class TreeNodeAttributeMapper<T> {

    /**
     * 当前节点 id 的别名
     */
    private String nodeIdName = "id";

    /**
     * 覆盖节点 id 对应数值的接口
     */
    private Function<T, Object> overNodeIdFunction;

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
     * 当前节点的排序变量的别名，不设置则不展示
     */
    private String sortName = "sort";

    /**
     * 扩展变量的别名
     */
    private String extraName = "extra";

    /**
     * 扩展变量 key 和 value 映射 list
     */
    private List<ExtraMapping<T>> extraMappingList;

    private TreeNodeAttributeMapper() {
    }

    public static <T> TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder<T> builder() {
        return new TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder<T>();
    }

    public static class TreeNodeAttributeMapperBuilder<T> {
        private String nodeIdName;
        private Function<T, Object> overNodeIdFunction;
        private String nodeName;
        private String parentId;
        private String childrenName;
        private String dataName;
        /**
         * 排序值名称，设置为 null 或者 空字符串则不展示
         */
        private String sortName;

        /**
         * 扩展值名称，设置为 null 或者 空字符串则不展示
         */
        private String extraName;

        private List<ExtraMapping<T>> extraMappingList;

        TreeNodeAttributeMapperBuilder() {
        }

        public TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder<T> nodeIdName(String nodeIdName) {
            this.nodeIdName = nodeIdName;
            return this;
        }

        public TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder<T> overNodeId(Function<T, Object> overNodeIdFunction) {
            this.overNodeIdFunction = overNodeIdFunction;
            return this;
        }

        public TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder<T> overNodeId(String nodeId, Function<T, Object> overNodeIdFunction) {
            this.nodeIdName = nodeId;
            this.overNodeIdFunction = overNodeIdFunction;
            return this;
        }

        public TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder<T> nodeName(String nodeName) {
            this.nodeName = nodeName;
            return this;
        }

        public TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder<T> parentId(String parentId) {
            this.parentId = parentId;
            return this;
        }

        public TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder<T> childrenName(String childrenName) {
            this.childrenName = childrenName;
            return this;
        }

        public TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder<T> dataName(String dataName) {
            this.dataName = dataName;
            return this;
        }

        public TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder<T> sortName(String sortName) {
            this.sortName = sortName;
            return this;
        }

        /**
         * 设置字段变量的别名
         * 扩展字段的别名会覆盖掉已有的数据字段数据
         *
         * @param extraName 扩展字段变量的别名
         * @return TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder
         */
        public TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder<T> extraName(String extraName) {
            this.extraName = extraName;
            return this;
        }

        public TreeNodeAttributeMapper.TreeNodeAttributeMapperBuilder<T> extraMappingList(List<ExtraMapping<T>> extraMappingList) {
            this.extraMappingList = extraMappingList;
            return this;
        }

        public TreeNodeAttributeMapper<T> build() {
            return new TreeNodeAttributeMapper<T>()
                    .setNodeIdName(this.nodeIdName)
                    .setOverNodeIdFunction(overNodeIdFunction)
                    .setNodeName(this.nodeName)
                    .setParentId(this.parentId)
                    .setChildrenName(this.childrenName)
                    .setDataName(this.dataName)
                    .setSortName(this.sortName)
                    .setExtraName(this.extraName)
                    .setExtraMappingList(this.extraMappingList);
        }
    }

    public TreeNodeAttributeMapper<T> setNodeIdName(String nodeIdName) {
        if (isNotBlank(nodeIdName)) {
            this.nodeIdName = nodeIdName;
        }
        return this;
    }

    public TreeNodeAttributeMapper<T> setOverNodeIdFunction(Function<T, Object> overNodeIdFunction) {
        if (nonNull(overNodeIdFunction)) {
            this.overNodeIdFunction = overNodeIdFunction;
        }
        return this;
    }

    public TreeNodeAttributeMapper<T> setNodeName(String nodeName) {
        if (isNotBlank(nodeName)) {
            this.nodeName = nodeName;
        }
        return this;
    }

    public TreeNodeAttributeMapper<T> setParentId(String parentId) {
        if (isNotBlank(parentId)) {
            this.parentId = parentId;
        }
        return this;
    }

    public TreeNodeAttributeMapper<T> setChildrenName(String childrenName) {
        if (isNotBlank(childrenName)) {
            this.childrenName = childrenName;
        }
        return this;
    }

    public TreeNodeAttributeMapper<T> setDataName(String dataName) {
        if (isNotBlank(dataName)) {
            this.dataName = dataName;
        }
        return this;
    }

    public TreeNodeAttributeMapper<T> setSortName(String sortName) {
        if (isNotBlank(sortName)) {
            this.sortName = sortName;
        } else {
            this.sortName = null;
        }
        return this;
    }

    public TreeNodeAttributeMapper<T> setExtraName(String extraName) {
        if (isNotBlank(extraName)) {
            this.extraName = extraName;
        } else {
            this.extraName = null;
        }
        return this;
    }

    public TreeNodeAttributeMapper<T> setExtraMappingList(List<ExtraMapping<T>> extraMappingList) {
        this.extraMappingList = extraMappingList;
        return this;
    }

    public String getNodeIdName() {
        return nodeIdName;
    }

    public Function<T, Object> getOverNodeIdFunction() {
        return overNodeIdFunction;
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

    public String getExtraName() {
        return extraName;
    }

    public List<ExtraMapping<T>> getExtraMappingList() {
        return extraMappingList;
    }

    public static class ExtraMapping<T> {
        private String extraKeyName;
        private Function<T, Object> extraFunction;

        public ExtraMapping(String extraKeyName, Function<T, Object> extraFunction) {
            this.extraKeyName = extraKeyName;
            this.extraFunction = extraFunction;
        }

        public String getExtraKeyName() {
            return extraKeyName;
        }

        public Function<T, Object> getExtraFunction() {
            return extraFunction;
        }
    }
}
