package org.woodwhales.business.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private Integer sort;

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
    public static <K, T> TreeNode<K, T> build(T data, TreeNodeGenerator<K, T> treeNodeGenerator, boolean withData) {
        TreeNode<K, T> treeNode = new TreeNode();
        treeNode.setId(treeNodeGenerator.getId(data));

        if(withData) {
            treeNode.setData(data);
        }

        treeNode.setName(treeNodeGenerator.getName(data));
        treeNode.setParentId(treeNodeGenerator.getParentId(data));
        treeNode.setSort(Objects.isNull(treeNodeGenerator.getSort(data)) ? 0 : treeNodeGenerator.getSort(data));
        return treeNode;
    }

    public static <K, T> Map<String, Object> toMap(TreeNode<K, T> treeNode, TreeNodeAttributeMapper treeAttributeMapper, boolean withData) {
        Map<String, Object> map = new HashMap<>(6);
        map.put(treeAttributeMapper.getNodeId(), treeNode.getId());
        map.put(treeAttributeMapper.getNodeName(), treeNode.getName());
        map.put(treeAttributeMapper.getParentId(), treeNode.getParentId());
        map.put(treeAttributeMapper.getSortName(), treeNode.getSort());

        if(isNotEmpty(treeNode.getChildren())) {
            map.put(treeAttributeMapper.getChildrenName(), treeNode.getChildren().stream()
                    .map(node -> TreeNode.toMap(node, treeAttributeMapper, withData))
                    .collect(Collectors.toList()));
        } else {
            map.put(treeAttributeMapper.getChildrenName(), null);
        }

        if(withData) {
            map.put(treeAttributeMapper.getDataName(), treeNode.getData());
        } else {
            map.put(treeAttributeMapper.getDataName(), null);
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

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setChildren(List<TreeNode<K, T>> children) {
        this.children = children;
    }

    public List<TreeNode<K, T>> getChildren() {
        return children;
    }
}
