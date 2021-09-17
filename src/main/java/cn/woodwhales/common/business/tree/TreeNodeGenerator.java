package cn.woodwhales.common.business.tree;

/**
 * @author woodwhales on 2020-12-11
 * @description TreeNode 生成器
 */
public interface TreeNodeGenerator<K, T> {

    /**
     * 获取当前节点 id
     * @param data
     * @return
     */
    K getId(T data);

    /**
     * 获取父级节点
     * @param data
     * @return
     */
    K getParentId(T data);

    /**
     * 获取节点名称
     * @param data
     * @return
     */
    String getName(T data);

    /**
     * 是否为根节点
     * @param data
     * @return
     */
    boolean isRootNode(T data);

    /**
     * 获取排序字段
     * @param data
     * @return
     */
    int getSort(T data);

}
