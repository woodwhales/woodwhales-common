package cn.woodwhales.common.business.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * @author woodwhales on 2020-12-11
 * @description 通用节点容器对象
 */
public class TreeNode<K, T> {

    /**
     * ID
     */
    private K id;

    /**
     * 父节点ID
     */
    private K parentId;

    /**
     * 当前节点的名称
     */
    private String name;

    /**
     * 源数据
     */
    private T data;

    /**
     * 子节点集合
     */
    private List<TreeNode<K, T>> children;

    /**
     * 排序值
     */
    private int sort;

    private TreeNode() {
    }

    /**
     * 构建 TreeNode 对象
     * @param data 源数据
     * @param treeNodeGenerator TreeNode 生成器
     * @param <K> 节点 id 类型
     * @param <T> 源数据类型
     * @return
     */
    public static <K, T> TreeNode<K, T> build(T data, TreeNodeGenerator<K, T> treeNodeGenerator) {
        return build(data, treeNodeGenerator, false);
    }

    /**
     * 构建 TreeNode 对象
     * @param data
     * @param treeNodeGenerator
     * @param withData
     * @param <K>
     * @param <T>
     * @return
     */
    public static <K, T> TreeNode<K, T> build(T data,
                                              TreeNodeGenerator<K, T> treeNodeGenerator,
                                              boolean withData) {
        TreeNode<K, T> treeNode = new TreeNode<>();
        treeNode.setId(treeNodeGenerator.getId(data));

        if(withData) {
            treeNode.setData(data);
        }

        treeNode.setName(treeNodeGenerator.getName(data));
        treeNode.setParentId(treeNodeGenerator.getParentId(data));
        treeNode.setSort(treeNodeGenerator.getSort(data));
        return treeNode;
    }

    public static <K, T> Map<String, Object> toMap(TreeNode<K, T> treeNode,
                                                   final TreeNodeAttributeMapper<T> treeAttributeMapper,
                                                   final Function<T, Object> extraFunction,
                                                   final boolean withData,
                                                   final boolean needDropData) {
        Map<String, Object> map = new HashMap<>(7);

        Function<T, Object> overNodeIdFunction = treeAttributeMapper.getOverNodeIdFunction();
        T data = treeNode.getData();

        // 设置当前节点id
        if(nonNull(overNodeIdFunction) && nonNull(data)) {
            map.put(treeAttributeMapper.getNodeId(), overNodeIdFunction.apply(data));
        } else {
            map.put(treeAttributeMapper.getNodeId(), treeNode.getId());
        }

        // 设置当前节点的名称
        map.put(treeAttributeMapper.getNodeName(), treeNode.getName());
        // 设置当前节点的父节点
        map.put(treeAttributeMapper.getParentId(), treeNode.getParentId());
        // 设置当前节点的排序值
        map.put(treeAttributeMapper.getSortName(), treeNode.getSort());
        // 设置当前节点的子节点
        if(isNotEmpty(treeNode.getChildren())) {
            map.put(treeAttributeMapper.getChildrenName(), treeNode.getChildren().stream()
                    .map(node -> TreeNode.toMap(node, treeAttributeMapper, extraFunction, withData, needDropData))
                    .collect(toList()));
        } else {
            map.put(treeAttributeMapper.getChildrenName(), null);
        }

        // 设置当前节点是否携带源数据
        if(withData) {
            map.put(treeAttributeMapper.getDataName(), data);
        } else {
            map.put(treeAttributeMapper.getDataName(), null);
        }

        // 设置当前节点的扩展数据
        if(nonNull(extraFunction) && nonNull(data)) {
            map.put(treeAttributeMapper.getExtraName(), extraFunction.apply(data));
        } else {
            map.put(treeAttributeMapper.getExtraName(), null);
        }

        if(needDropData) {
            map.remove(treeAttributeMapper.getDataName());
        }

        return map;
    }

    public K getId() {
        return id;
    }

    public K getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getData() {
        return data;
    }

    public Integer getSort() {
        return sort;
    }

    public void setId(K id) {
        this.id = id;
    }

    public void setParentId(K parentId) {
        this.parentId = parentId;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setChildren(List<TreeNode<K, T>> children) {
        this.children = children;
    }

    public List<TreeNode<K, T>> getChildren() {
        return children;
    }
}
