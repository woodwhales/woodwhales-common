package org.woodwhales.business.tree;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * @author woodwhales on 2020-12-10
 * @description
 */
public class TreeTool {

    /**
     *
     * @param sourceList 源数据集合
     * @param treeNodeGenerator TreeNode 生成器
     * @param treeNodeAttributeMapper 属性名映射器
     * @param <K> 节点 id 类型
     * @param <T> 数据源类型
     * @return
     */
    public static <K, T> List<Map<String, Object>> tree(List<T> sourceList,
                                                        final TreeNodeGenerator<K, T> treeNodeGenerator,
                                                        final TreeNodeAttributeMapper treeNodeAttributeMapper) {
        return tree(sourceList, treeNodeGenerator, treeNodeAttributeMapper, false);
    }

    /**
     *
     * @param sourceList 源数据集合
     * @param treeNodeGenerator TreeNode 生成器
     * @param treeNodeAttributeMapper 属性名映射器
     * @param withData 是否携带源数据
     * @param <K> 节点 id 类型
     * @param <T> 数据源类型
     * @return
     */
    public static <K, T> List<Map<String, Object>> tree(List<T> sourceList,
                                                        final TreeNodeGenerator<K, T> treeNodeGenerator,
                                                        final TreeNodeAttributeMapper treeNodeAttributeMapper,
                                                        final boolean withData) {
        if(isEmpty(sourceList)) {
            return emptyList();
        }

        List<TreeNode<K, T>> treeNodeList = tree(sourceList, treeNodeGenerator, withData);

        return treeNodeList.stream()
                            .map(treeNode -> TreeNode.toMap(treeNode, treeNodeAttributeMapper, withData))
                            .collect(Collectors.toList());
    }

    /**
     * 树化数据
     * @param sourceList 源数据集合
     * @param treeNodeGenerator TreeNode 生成器
     * @param <K> 节点 id 类型
     * @param <T> 数据源类型
     * @return
     */
    public static <K, T> List<TreeNode<K, T>> tree(List<T> sourceList,
                                                   final TreeNodeGenerator<K, T> treeNodeGenerator) {
        return tree(sourceList, treeNodeGenerator, false);
    }

    /**
     * 树化数据
     * @param sourceList 源数据集合
     * @param treeNodeGenerator TreeNode 生成器
     * @param <K> 节点 id 类型
     * @param <T> 数据源类型
     * @param withData 是否携带源数据
     * @return
     */
    public static <K, T> List<TreeNode<K, T>> tree(List<T> sourceList,
                                                   final TreeNodeGenerator<K, T> treeNodeGenerator,
                                                   final boolean withData) {
        if(isEmpty(sourceList)) {
            return emptyList();
        }

        List<TreeNode<K, T>> rootNodeList = sourceList.stream()
                                                .filter(source -> treeNodeGenerator.isRootNode(source))
                                                .map(source -> TreeNode.build(source, treeNodeGenerator, withData))
                                                .sorted(Comparator.comparing(TreeNode::getSort))
                                                .collect(Collectors.toList());

        if(isEmpty(rootNodeList)) {
            return emptyList();
        }

        Map<K, List<TreeNode<K, T>>> unRootNodeContainer = sourceList.stream()
                                                    .filter(source -> !treeNodeGenerator.isRootNode(source))
                                                    .map(source -> TreeNode.build(source, treeNodeGenerator, withData))
                                                    .sorted(Comparator.comparing(TreeNode::getSort))
                                                    .collect(Collectors.groupingBy(TreeNode::getParentId));

        for (TreeNode<K, T> treeNode : rootNodeList) {
            treeNode.setChildren(buildChildren(treeNode.getId(), unRootNodeContainer));
        }

        return rootNodeList;
    }

    /**
     * 获取当前节点的所有子节点
     * @param currentNodeId 当前节点
     * @param unRootNodeContainer 所有非根节点的 map 集合（key 为父节点）
     * @param <K> 节点 id 类型
     * @param <T> 数据源类型
     * @return
     */
    private static <K, T> List<TreeNode<K, T>> buildChildren(final K currentNodeId,
                                                             Map<K, List<TreeNode<K, T>>> unRootNodeContainer) {
        List<TreeNode<K, T>> childrenList = unRootNodeContainer.get(currentNodeId);
        if(isEmpty(childrenList)) {
            return null;
        }

        for (TreeNode<K, T> treeNode : childrenList) {
            treeNode.setChildren(buildChildren(treeNode.getId(), unRootNodeContainer));
        }

        return childrenList;
    }
}
