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
