package org.woodwhales.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.woodwhales.business.DataTool;
import org.woodwhales.common.model.field.PageQueryInterface;
import org.woodwhales.common.model.vo.PageRespVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * @author woodwhales on 2021-01-05 14:02
 * @description mybatisPlus sql 执行器
 */
public class MybatisPlusExecutor {

    /**
     * 查询数据集合
     * @param mapper
     * @param wrapper
     * @param mapping
     * @param <Entity>
     * @param <Mapper>
     * @param <DTO>
     * @return
     */
    public static <Entity, Mapper extends BaseMapper<Entity>, DTO> List<DTO> executeQueryList(Mapper mapper,
                                                                                              Wrapper<Entity> wrapper,
                                                                                              Function<? super Entity, ? extends DTO> mapping) {
        List<Entity> entityList = mapper.selectList(wrapper);

        if (isEmpty(entityList)) {
            return new ArrayList<>();
        }

        return DataTool.toList(entityList, mapping);
    }

    /**
     * 查询数据集合
     * @param mapper
     * @param lambdaQueryWrapperConsumer
     * @param mapping
     * @param <Entity>
     * @param <Mapper>
     * @param <DTO>
     * @return
     */
    public static <Entity, Mapper extends BaseMapper<Entity>, DTO> List<DTO> executeQueryList(Mapper mapper,
                                                                                              Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer,
                                                                                              Function<? super Entity, ? extends DTO> mapping) {
        LambdaQueryWrapper<Entity> wrapper = Wrappers.<Entity>lambdaQuery();
        return executeQueryList(mapper, wrapper, mapping);
    }

    /**
     * 查询数据集合
     * @param mapper
     * @param wrapper
     * @param <Entity>
     * @param <Mapper>
     * @return
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> List<Entity> executeQueryList(Mapper mapper,
                                                                                            Wrapper<Entity> wrapper) {
        List<Entity> entityList = mapper.selectList(wrapper);
        if (isEmpty(entityList)) {
            return new ArrayList<>();
        }
        return entityList;
    }

    /**
     * 查询数据集合
     * @param mapper
     * @param lambdaQueryWrapperConsumer
     * @param <Entity>
     * @param <Mapper>
     * @return
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> List<Entity> executeQueryList(Mapper mapper,
                                                                                            Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer) {
        LambdaQueryWrapper<Entity> wrapper = Wrappers.<Entity>lambdaQuery();
        lambdaQueryWrapperConsumer.accept(wrapper);
        return executeQueryList(mapper, wrapper);
    }

    /**
     * 查询数据并按照 keyMapping 规则转成 map 集合
     * @param mapper
     * @param wrapper
     * @param mapping
     * @param keyMapping
     * @param <Entity>
     * @param <Mapper>
     * @param <DTO>
     * @param <Key>
     * @return
     */
    public static <Entity, Mapper extends BaseMapper<Entity>, DTO, Key> Map<Key, DTO> executeQueryListMap(Mapper mapper,
                                                                                                          Wrapper<Entity> wrapper,
                                                                                                          Function<? super Entity, ? extends DTO> mapping,
                                                                                                          Function<? super DTO, ? extends Key> keyMapping) {
        return DataTool.toMap(executeQueryList(mapper, wrapper, mapping), keyMapping);
    }

    /**
     * 查询数据并按照 keyMapping 规则转成 map 集合
     * @param mapper
     * @param lambdaQueryWrapperConsumer
     * @param mapping
     * @param keyMapping
     * @param <Entity>
     * @param <Mapper>
     * @param <DTO>
     * @param <Key>
     * @return
     */
    public static <Entity, Mapper extends BaseMapper<Entity>, DTO, Key> Map<Key, DTO> executeQueryListMap(Mapper mapper,
                                                                                                          Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer,
                                                                                                          Function<? super Entity, ? extends DTO> mapping,
                                                                                                          Function<? super DTO, ? extends Key> keyMapping) {
        LambdaQueryWrapper<Entity> wrapper = Wrappers.<Entity>lambdaQuery();
        lambdaQueryWrapperConsumer.accept(wrapper);
        return DataTool.toMap(executeQueryList(mapper, wrapper, mapping), keyMapping);
    }

    /**
     * 查询单条数据
     * @param mapper
     * @param wrapper
     * @param mapping
     * @param <Entity>
     * @param <Mapper>
     * @param <DTO>
     * @return
     */
    public static <Entity, Mapper extends BaseMapper<Entity>, DTO> DTO executeQueryOne(Mapper mapper,
                                                                                       Wrapper<Entity> wrapper,
                                                                                       Function<? super Entity, ? extends DTO> mapping) {
        Entity entity = mapper.selectOne(wrapper);
        if (isNull(entity)) {
            return null;
        }

        return mapping.apply(entity);
    }

    /**
     * 查询单条数据
     * @param mapper
     * @param lambdaQueryWrapperConsumer
     * @param mapping
     * @param <Entity>
     * @param <Mapper>
     * @param <DTO>
     * @return
     */
    public static <Entity, Mapper extends BaseMapper<Entity>, DTO> DTO executeQueryOne(Mapper mapper,
                                                                                       Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer,
                                                                                       Function<? super Entity, ? extends DTO> mapping) {
        LambdaQueryWrapper<Entity> wrapper = Wrappers.<Entity>lambdaQuery();
        lambdaQueryWrapperConsumer.accept(wrapper);
        return executeQueryOne(mapper, lambdaQueryWrapperConsumer, mapping);
    }

    /**
     * 查询单条数据
     * @param mapper
     * @param wrapper
     * @param <Entity>
     * @param <Mapper>
     * @return
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> Entity executeQueryOne(Mapper mapper,
                                                                                     Wrapper<Entity> wrapper) {
        Entity entity = mapper.selectOne(wrapper);
        if (isNull(entity)) {
            return null;
        }

        return entity;
    }

    /**
     * 分页查询数据
     * @param mapper
     * @param queryParam
     * @param lambdaQueryWrapperConsumer
     * @param mapping
     * @param <Entity>
     * @param <DTO>
     * @param <Mapper>
     * @return
     */
    public static <Entity, DTO, Mapper extends BaseMapper<Entity>> PageRespVO<DTO> executeQueryPage(Mapper mapper,
                                                                                        PageQueryInterface queryParam,
                                                                                        Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer,
                                                                                        Function<Entity, DTO> mapping) {
        IPage<Entity> pageResult = executeSelectPage(mapper, queryParam, lambdaQueryWrapperConsumer);
        return PageRespVO.buildPageRespVO(pageResult, mapping);
    }

    /**
     * 分页查询数据
     * @param mapper
     * @param queryParam
     * @param lambdaQueryWrapperConsumer
     * @param <Entity>
     * @param <Mapper>
     * @return
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> PageRespVO<Entity> executeQueryPage(Mapper mapper,
                                                                                                  PageQueryInterface queryParam,
                                                                                                  Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer) {
        IPage<Entity> pageResult = executeSelectPage(mapper, queryParam, lambdaQueryWrapperConsumer);
        return PageRespVO.buildPageRespVO(pageResult);
    }

    /**
     * 分页查询数据
     * @param mapper
     * @param queryParam
     * @param wrapper
     * @param <Entity>
     * @param <Mapper>
     * @return
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> PageRespVO<Entity> executeQueryPage(Mapper mapper,
                                                                                                  PageQueryInterface queryParam,
                                                                                                  Wrapper<Entity> wrapper) {
        Page<Entity> page = new Page<>(queryParam.getPage(), queryParam.getLimit());
        IPage<Entity> pageResult = mapper.selectPage(page, wrapper);
        return PageRespVO.buildPageRespVO(pageResult);
    }

    /**
     * 分页查询数据
     * @param mapper
     * @param queryParam
     * @param lambdaQueryWrapperConsumer
     * @param <Entity>
     * @param <Mapper>
     * @return
     */
    private static <Entity, Mapper extends BaseMapper<Entity>> IPage<Entity> executeSelectPage(Mapper mapper,
                                                                                               PageQueryInterface queryParam,
                                                                                               Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer) {
        Page<Entity> page = new Page<>(queryParam.getPage(), queryParam.getLimit());
        LambdaQueryWrapper<Entity> wrapper = Wrappers.<Entity>lambdaQuery();
        lambdaQueryWrapperConsumer.accept(wrapper);
        return mapper.selectPage(page, wrapper);
    }

    /**
     * 批量插入数据
     * @param batchMapper
     * @param entityList
     * @param <Mapper>
     * @param <Entity>
     * @return
     */
    public static <Mapper extends BaseMapper<Entity>, Entity> boolean batchInsert(ServiceImpl<Mapper, Entity> batchMapper, List<Entity> entityList) {
        if(isEmpty(entityList)) {
            return true;
        }
        return batchMapper.saveBatch(entityList);
    }

    /**
     * 批量插入数据
     * @param batchMapper
     * @param mapping
     * @param dtoList
     * @param <Mapper>
     * @param <Entity>
     * @param <DTO>
     * @return
     */
    public static <Mapper extends BaseMapper<Entity>, Entity, DTO> boolean batchInsert(ServiceImpl<Mapper, Entity> batchMapper,
                                                                                       Function<List<DTO>, List<Entity>> mapping,
                                                                                       List<DTO> dtoList) {
        return batchMapper.saveBatch(mapping.apply(dtoList));
    }

    /**
     * 按照 entityIdFunction 更新数据
     * @param mapper
     * @param entity
     * @param entityIdFunction
     * @param function
     * @param <Mapper>
     * @param <Entity>
     * @param <K>
     * @return
     */
    public static <Mapper extends BaseMapper<Entity>, Entity, K extends Serializable> int update(Mapper mapper,
                                                                                                 Entity entity,
                                                                                                 Function<Entity, K> entityIdFunction,
                                                                                                 Function<Entity, Entity> function) {
        Entity entityFromDB = mapper.selectById(entityIdFunction.apply(entity));
        if(isNull(entityFromDB)) {
            return 0;
        }
        return mapper.updateById(function.apply(entityFromDB));
    }

}
