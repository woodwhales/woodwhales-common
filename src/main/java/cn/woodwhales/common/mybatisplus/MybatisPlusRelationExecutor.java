package cn.woodwhales.common.mybatisplus;

import cn.woodwhales.common.business.DataTool;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 一对多查询工具
 * @author woodwhales on 2022-09-05 9:28
 */
public class MybatisPlusRelationExecutor {

    /**
     * 一对多查询
     * @param leftId 业务id
     * @param leftService 业务service
     * @param getLeftIdFunction 获取业务对象主键
     * @param relationService 获取关系service
     * @param getLeftIdRelationFunction 获取业务id的接口
     * @return 业务id对应的业务关系对象集合
     * @param <LeftId> 业务id类型
     * @param <LeftService> 业务关系的id类型
     * @param <LeftMapper> 业务mapper类型
     * @param <LeftEntity> 业务对象类型
     * @param <RelationService> 业务关系service类型
     * @param <RelationMapper> 业务关系mapper类型
     * @param <RelationEntity> 业务关系对象类型
     */
    public static <LeftId extends Serializable,
                    LeftService extends ServiceImpl<LeftMapper, LeftEntity>,
                    LeftMapper extends BaseMapper<LeftEntity>,
                    LeftEntity,
                    RelationService extends ServiceImpl<RelationMapper, RelationEntity>,
                    RelationMapper extends BaseMapper<RelationEntity>,
                    RelationEntity> List<RelationEntity> executeQuery(LeftId leftId,
                                                                      LeftService leftService,
                                                                      Function<LeftEntity, ?> getLeftIdFunction,
                                                                      RelationService relationService,
                                                                      SFunction<RelationEntity, LeftId> getLeftIdRelationFunction) {
        LeftEntity leftEntity = leftService.getById(leftId);
        if(Objects.isNull(leftEntity)) {
            return Collections.emptyList();
        }
        List<RelationEntity> relationList = relationService.list(Wrappers.<RelationEntity>lambdaQuery()
                                                            .eq(getLeftIdRelationFunction, getLeftIdFunction.apply(leftEntity)));
        if(CollectionUtils.isEmpty(relationList)) {
            return Collections.emptyList();
        }
        return  relationList;
    }

    /**
     * 多对多查询
     * <p>
     * 通过用户查询对应的角色列表
     * user - roleList
     *
     * user1 - role1
     *       - role2
     *       - role3
     * </p>
     * user [user主键]
     * user_role [user主键,role主键]
     * role [role主键]
     *
     * @param leftId 业务id
     * @param leftService 业务service
     * @param getLeftIdFunction 获取业务对象主键
     * @param relationService 业务关系对象service
     * @param getLeftIdRelationFunction 获取业务id的接口
     * @param getRightIdRelationFunction 获取关系对象id的接口
     * @param rightService 获取关系service
     * @param getRightIdFunction 获取业务关系对象id
     * @return 业务id对应的业务关系对象集合
     * @param <LeftId> 业务id类型
     * @param <RightId> 业务关系的id类型
     * @param <LeftService> 业务service类型
     * @param <LeftMapper> 业务mapper类型
     * @param <LeftEntity> 业务对象类型
     * @param <RelationService> 业务关系service类型
     * @param <RelationMapper>  业务关系mapper类型
     * @param <RelationEntity> 业务关系对象类型
     * @param <RightService> 业务关系service类型
     * @param <RightMapper> 业务关系mapper类型
     * @param <RightEntity> 业务关系对象类型
     */
    public static <LeftId extends Serializable,
                    RightId,
                    LeftService extends ServiceImpl<LeftMapper, LeftEntity>,
                    LeftMapper extends BaseMapper<LeftEntity>,
                    LeftEntity,
                    RelationService extends ServiceImpl<RelationMapper, RelationEntity>,
                    RelationMapper extends BaseMapper<RelationEntity>,
                    RelationEntity,
                    RightService extends ServiceImpl<RightMapper, RightEntity>,
                    RightMapper extends BaseMapper<RightEntity>,
                    RightEntity> List<RightEntity> executeQuery(LeftId leftId,
                                                                LeftService leftService,
                                                                Function<LeftEntity, ?> getLeftIdFunction,
                                                                RelationService relationService,
                                                                SFunction<RelationEntity, LeftId> getLeftIdRelationFunction,
                                                                Function<RelationEntity, RightId> getRightIdRelationFunction,
                                                                RightService rightService,
                                                                SFunction<RightEntity, ?> getRightIdFunction) {
        LeftEntity leftEntity = leftService.getById(leftId);
        if(Objects.isNull(leftEntity)) {
            return Collections.emptyList();
        }
        List<RelationEntity> relationList = relationService.list(Wrappers.<RelationEntity>lambdaQuery()
                                                            .eq(getLeftIdRelationFunction, getLeftIdFunction.apply(leftEntity)));
        if(CollectionUtils.isEmpty(relationList)) {
            return Collections.emptyList();
        }
        List<RightId> rightIdList = DataTool.toList(relationList, getRightIdRelationFunction);
        return rightService.list(Wrappers.<RightEntity>lambdaQuery()
                        .in(getRightIdFunction, rightIdList));
    }

}
