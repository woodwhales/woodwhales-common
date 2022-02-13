package cn.woodwhales.common.business.tree;

/**
 * @author woodwhales on 2020-12-11
 * TreeNode 生成器
 */
public interface TreeNodeGenerator<K, T> {

    /**
     * 获取当前节点 id
     *
     * @param data 原始数据
     * @return 获取当前节点 id
     */
    K getId(T data);

    /**
     * 获取父级节点
     *
     * @param data 原始数据
     * @return 获取父级节点
     */
    K getParentId(T data);

    /**
     * 获取节点名称
     *
     * @param data 原始数据
     * @return 获取节点名称
     */
    String getName(T data);

    /**
     * 是否为根节点
     *
     * @param data 原始数据
     * @return 是否为根节点
     */
    boolean isRootNode(T data);

    /**
     * 获取排序字段
     *
     * @param data 原始数据
     * @return 获取排序字段
     */
    int getSort(T data);

}
