package cn.woodwhales.common.business.tree;

import cn.woodwhales.common.business.tree.TreeNodeAttributeMapper.ExtraMapping;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * @author woodwhales on 2020-12-11
 * 通用节点容器对象
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
     *
     * @param data              原始数据
     * @param treeNodeGenerator 树节点生成器
     * @param <K>               key泛型
     * @param <T>               value泛型
     * @return 返回数节点对象，根节点
     */
    public static <K, T> TreeNode<K, T> build(T data, TreeNodeGenerator<K, T> treeNodeGenerator) {
        TreeNode<K, T> treeNode = new TreeNode<>();
        treeNode.setId(treeNodeGenerator.getId(data));
        treeNode.setData(data);
        treeNode.setName(treeNodeGenerator.getName(data));
        treeNode.setParentId(treeNodeGenerator.getParentId(data));
        treeNode.setSort(treeNodeGenerator.getSort(data));
        return treeNode;
    }

    public static <K, T> TreeNode<K, T> build(T data,
                                              TreeNodeGenerator<K, T> treeNodeGenerator,
                                              TreeNodeAttributeMapper<T> treeNodeAttributeMapper) {
        TreeNode<K, T> treeNode = new TreeNode<>();
        treeNode.setId(treeNodeGenerator.getId(data));
        treeNode.setData(data);
        treeNode.setName(treeNodeGenerator.getName(data));
        treeNode.setParentId(treeNodeGenerator.getParentId(data));
        if(StringUtils.isNotBlank(treeNodeAttributeMapper.getSortName())) {
            treeNode.setSort(treeNodeGenerator.getSort(data));
        }
        return treeNode;
    }

    public static <K, T> Map<String, Object> toMap(TreeNode<K, T> treeNode,
                                                   final TreeNodeAttributeMapper<T> treeAttributeMapper,
                                                   final Function<T, Object> extraFunction,
                                                   final boolean withData) {
        return toMapProcess(treeNode,
                            treeAttributeMapper,
                            withData,
                            node -> toMap(node, treeAttributeMapper, extraFunction, withData),
                            (data, map) -> {
                                // 设置当前节点的扩展数据
                                List<ExtraMapping<T>> extraMappingList = treeAttributeMapper.getExtraMappingList();
                                if (CollectionUtils.isNotEmpty(extraMappingList) && nonNull(data)) {
                                    for (ExtraMapping<T> extraMapping : extraMappingList) {
                                        map.put(extraMapping.getExtraKeyName(), extraMapping.getExtraFunction().apply(data));
                                    }
                                }

                                // 设置当前节点的扩展数据
                                if(StringUtils.isNotBlank(treeAttributeMapper.getExtraName())) {
                                    if (nonNull(extraFunction) && nonNull(data)) {
                                        map.put(treeAttributeMapper.getExtraName(), extraFunction.apply(data));
                                    } else {
                                        map.put(treeAttributeMapper.getExtraName(), null);
                                    }
                                }
                            });
    }

    public static <K, T> Map<String, Object> toMapProcess(TreeNode<K, T> treeNode,
                                                          final TreeNodeAttributeMapper<T> treeAttributeMapper,
                                                          final boolean withData,
                                                          Function<TreeNode<K, T>, Map<String, Object>> childrenNodeToMapFunction,
                                                          BiConsumer<T, Map<String, Object>> extraConsumer) {
        Map<String, Object> map = new HashMap<>(16);
        Function<T, Object> overNodeIdFunction = treeAttributeMapper.getOverNodeIdFunction();
        T data = treeNode.getData();

        // 设置当前节点id
        if (nonNull(overNodeIdFunction) && nonNull(data)) {
            map.put(treeAttributeMapper.getNodeIdName(), overNodeIdFunction.apply(data));
        } else {
            map.put(treeAttributeMapper.getNodeIdName(), treeNode.getId());
        }

        // 设置当前节点的名称
        map.put(treeAttributeMapper.getNodeName(), treeNode.getName());
        // 设置当前节点的父节点
        map.put(treeAttributeMapper.getParentId(), treeNode.getParentId());

        // 设置当前节点的排序值
        if(StringUtils.isNotBlank(treeAttributeMapper.getSortName())) {
            map.put(treeAttributeMapper.getSortName(), treeNode.getSort());
        }

        // 设置当前节点的子节点
        if (isNotEmpty(treeNode.getChildren())) {
            map.put(treeAttributeMapper.getChildrenName(), treeNode.getChildren().stream()
                    .map(node -> childrenNodeToMapFunction.apply(node))
                    .collect(toList()));
        } else {
            map.put(treeAttributeMapper.getChildrenName(), null);
        }

        // 设置当前节点是否携带源数据
        if (withData && StringUtils.isNotBlank(treeAttributeMapper.getDataName())) {
            map.put(treeAttributeMapper.getDataName(), data);
        }

        extraConsumer.accept(data, map);
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
