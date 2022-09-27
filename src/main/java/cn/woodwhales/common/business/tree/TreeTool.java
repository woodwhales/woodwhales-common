package cn.woodwhales.common.business.tree;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * @author woodwhales on 2020-12-10
 */
public class TreeTool {

    /**
     * @param sourceList              源数据集合
     * @param treeNodeGenerator       TreeNode 生成器
     * @param treeNodeAttributeMapper 属性名映射器
     * @param <K>                     节点 id 类型
     * @param <T>                     数据源类型
     * @return list
     */
    public static <K, T> List<Map<String, Object>> tree(List<T> sourceList,
                                                        final TreeNodeGenerator<K, T> treeNodeGenerator,
                                                        final TreeNodeAttributeMapper<T> treeNodeAttributeMapper) {
        return tree(sourceList, treeNodeGenerator, treeNodeAttributeMapper, false);
    }

    /**
     * 树化数据，可支持添加扩展属性
     *
     * @param sourceList              源数据集合
     * @param treeNodeGenerator       TreeNode 生成器
     * @param treeNodeAttributeMapper 属性名映射器
     * @param extraFunction           添加扩展属性数据接口
     * @param <K>                     节点 id 类型
     * @param <T>                     数据源类型
     * @return list
     */
    public static <K, T> List<Map<String, Object>> tree(List<T> sourceList,
                                                        final TreeNodeGenerator<K, T> treeNodeGenerator,
                                                        final TreeNodeAttributeMapper<T> treeNodeAttributeMapper,
                                                        final Function<T, Object> extraFunction) {
        return tree(sourceList, treeNodeGenerator, treeNodeAttributeMapper, null, false);
    }

    /**
     * 树化数据，可支持添加扩展属性
     *
     * @param sourceList              源数据集合
     * @param treeNodeGenerator       TreeNode 生成器
     * @param treeNodeAttributeMapper 属性名映射器
     * @param extraFunction           添加扩展属性数据接口
     * @param withData                是否携带数据
     * @param <K>                     节点 id 类型
     * @param <T>                     数据源类型
     * @return list
     */
    public static <K, T> List<Map<String, Object>> tree(List<T> sourceList,
                                                        final TreeNodeGenerator<K, T> treeNodeGenerator,
                                                        final TreeNodeAttributeMapper<T> treeNodeAttributeMapper,
                                                        final Function<T, Object> extraFunction,
                                                        final boolean withData) {
        List<TreeNode<K, T>> rootNodeList = tree(sourceList, treeNodeGenerator);
        return rootNodeList.stream()
                .map(treeNode -> TreeNode.toMap(treeNode, treeNodeAttributeMapper, extraFunction, withData))
                .collect(toList());
    }

    /**
     * 树化数据
     *
     * @param sourceList              源数据集合
     * @param treeNodeGenerator       TreeNode 生成器
     * @param treeNodeAttributeMapper 属性名映射器
     * @param withData                是否携带源数据
     * @param <K>                     节点 id 类型
     * @param <T>                     数据源类型
     * @return list
     */
    public static <K, T> List<Map<String, Object>> tree(List<T> sourceList,
                                                        final TreeNodeGenerator<K, T> treeNodeGenerator,
                                                        final TreeNodeAttributeMapper<T> treeNodeAttributeMapper,
                                                        final boolean withData) {
        return tree(sourceList, treeNodeGenerator, treeNodeAttributeMapper, null, withData);
    }



    /**
     * 树化数据
     *
     * @param sourceList        源数据集合
     * @param treeNodeGenerator TreeNode 生成器
     * @param <K>               节点 id 类型
     * @param <T>               数据源类型
     * @return list
     */
    public static <K, T> List<TreeNode<K, T>> tree(List<T> sourceList,
                                                   final TreeNodeGenerator<K, T> treeNodeGenerator) {
        checkNotNull(treeNodeGenerator, "treeNodeGenerator must not null");
        if (isEmpty(sourceList)) {
            return emptyList();
        }

        List<TreeNode<K, T>> rootNodeList = sourceList.stream()
                .filter(treeNodeGenerator::isRootNode)
                .map(source -> TreeNode.build(source, treeNodeGenerator))
                .sorted(Comparator.comparing(TreeNode::getSort))
                .collect(toList());

        if (isEmpty(rootNodeList)) {
            return emptyList();
        }

        Map<K, List<TreeNode<K, T>>> unRootNodeContainer = sourceList.stream()
                .filter(source -> !treeNodeGenerator.isRootNode(source))
                .map(source -> TreeNode.build(source, treeNodeGenerator))
                .sorted(Comparator.comparing(TreeNode::getSort))
                .collect(Collectors.groupingBy(TreeNode::getParentId));

        for (TreeNode<K, T> treeNode : rootNodeList) {
            treeNode.setChildren(buildChildren(treeNode.getId(), unRootNodeContainer));
        }

        return rootNodeList;
    }

    /**
     * 获取当前节点的所有子节点
     *
     * @param currentNodeId       当前节点
     * @param unRootNodeContainer 所有非根节点的 map 集合（key 为父节点）
     * @param <K>                 节点 id 类型
     * @param <T>                 数据源类型
     * @return list
     */
    private static <K, T> List<TreeNode<K, T>> buildChildren(final K currentNodeId,
                                                             Map<K, List<TreeNode<K, T>>> unRootNodeContainer) {
        List<TreeNode<K, T>> childrenList = unRootNodeContainer.get(currentNodeId);
        if (isEmpty(childrenList)) {
            return null;
        }

        for (TreeNode<K, T> treeNode : childrenList) {
            treeNode.setChildren(buildChildren(treeNode.getId(), unRootNodeContainer));
        }

        return childrenList;
    }
}
