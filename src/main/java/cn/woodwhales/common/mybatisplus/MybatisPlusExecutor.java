package cn.woodwhales.common.mybatisplus;

import cn.woodwhales.common.business.DataTool;
import cn.woodwhales.common.model.field.PageQueryInterface;
import cn.woodwhales.common.model.param.SimplePageQueryParam;
import cn.woodwhales.common.model.vo.PageRespVO;
import cn.woodwhales.common.model.vo.RespVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * @author woodwhales on 2021-01-05 14:02
 * mybatisPlus sql 执行器
 */
public class MybatisPlusExecutor {

    /**
     * 查询数据集合
     *
     * @param mapper   mapper
     * @param wrapper  wrapper
     * @param mapping  mapping
     * @param <Entity> 数据实体泛型
     * @param <Mapper> mapper泛型
     * @param <DTO>    DTO
     * @return list
     */
    public static <Entity, Mapper extends BaseMapper<Entity>, DTO> List<DTO> list(Mapper mapper,
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
     *
     * @param service  mapper
     * @param wrapper  wrapper
     * @param mapping  mapping
     * @param <Entity> 数据实体泛型
     * @param <Mapper> mapper泛型
     * @param <DTO>    DTO
     * @return list
     */
    public static <Entity, Mapper extends BaseMapper<Entity>, DTO> List<DTO> list(ServiceImpl<Mapper, Entity> service,
                                                                                  Wrapper<Entity> wrapper,
                                                                                  Function<? super Entity, ? extends DTO> mapping) {
        List<Entity> entityList = service.list(wrapper);

        if (isEmpty(entityList)) {
            return new ArrayList<>();
        }

        return DataTool.toList(entityList, mapping);
    }

    /**
     * 查询数据集合
     *
     * @param mapper                     mapper
     * @param lambdaQueryWrapperConsumer lambdaQueryWrapperConsumer
     * @param mapping                    mapping
     * @param <Entity>                   数据实体泛型
     * @param <Mapper>                   mapper泛型
     * @param <DTO>                      DTO泛型
     * @return list
     */
    public static <Entity, Mapper extends BaseMapper<Entity>, DTO> List<DTO> list(Mapper mapper,
                                                                                  Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer,
                                                                                  Function<? super Entity, ? extends DTO> mapping) {
        LambdaQueryWrapper<Entity> wrapper = Wrappers.<Entity>lambdaQuery();
        return list(mapper, wrapper, mapping);
    }

    /**
     * 查询数据集合
     *
     * @param service                    service
     * @param lambdaQueryWrapperConsumer lambdaQueryWrapperConsumer
     * @param mapping                    mapping
     * @param <Entity>                   数据实体泛型
     * @param <Mapper>                   mapper泛型
     * @param <DTO>                      DTO泛型
     * @return list
     */
    public static <Entity, Mapper extends BaseMapper<Entity>, DTO> List<DTO> list(ServiceImpl<Mapper, Entity> service,
                                                                                  Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer,
                                                                                  Function<? super Entity, ? extends DTO> mapping) {
        LambdaQueryWrapper<Entity> wrapper = Wrappers.<Entity>lambdaQuery();
        return list(service, wrapper, mapping);
    }

    /**
     * 查询数据集合
     *
     * @param mapper   mapper
     * @param wrapper  wrapper
     * @param <Entity> 数据实体泛型
     * @param <Mapper> mapper泛型
     * @return list
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> List<Entity> list(Mapper mapper,
                                                                                Wrapper<Entity> wrapper) {
        List<Entity> entityList = mapper.selectList(wrapper);
        if (isEmpty(entityList)) {
            return new ArrayList<>();
        }
        return entityList;
    }

    /**
     * 查询数据集合
     *
     * @param service  service
     * @param wrapper  wrapper
     * @param <Entity> 数据实体泛型
     * @param <Mapper> mapper泛型
     * @return list
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> List<Entity> list(ServiceImpl<Mapper, Entity> service,
                                                                                Wrapper<Entity> wrapper) {
        List<Entity> entityList = service.list(wrapper);
        if (isEmpty(entityList)) {
            return new ArrayList<>();
        }
        return entityList;
    }

    /**
     * 查询数据集合
     *
     * @param mapper                     mapper
     * @param lambdaQueryWrapperConsumer lambdaQueryWrapperConsumer
     * @param <Entity>                   数据实体泛型
     * @param <Mapper>                   mapper泛型
     * @return list
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> List<Entity> list(Mapper mapper,
                                                                                Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer) {
        LambdaQueryWrapper<Entity> wrapper = Wrappers.<Entity>lambdaQuery();
        lambdaQueryWrapperConsumer.accept(wrapper);
        return list(mapper, wrapper);
    }

    /**
     * 查询数据集合
     *
     * @param service                    service
     * @param lambdaQueryWrapperConsumer lambdaQueryWrapperConsumer
     * @param <Entity>                   数据实体泛型
     * @param <Mapper>                   mapper泛型
     * @return list
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> List<Entity> list(ServiceImpl<Mapper, Entity> service,
                                                                                Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer) {
        LambdaQueryWrapper<Entity> wrapper = Wrappers.<Entity>lambdaQuery();
        lambdaQueryWrapperConsumer.accept(wrapper);
        return list(service, wrapper);
    }

    /**
     * 查询数据并按照 keyMapping 规则转成 map 集合
     *
     * @param mapper     mapper
     * @param wrapper    wrapper
     * @param mapping    mapping
     * @param keyMapping keyMapping
     * @param <Entity>   数据实体泛型
     * @param <Mapper>   mapper泛型
     * @param <DTO>      DTO泛型
     * @param <Key>      key泛型
     * @return list
     */
    public static <Entity, Mapper extends BaseMapper<Entity>, DTO, Key> Map<Key, DTO> list(Mapper mapper,
                                                                                           Wrapper<Entity> wrapper,
                                                                                           Function<? super Entity, ? extends DTO> mapping,
                                                                                           Function<? super DTO, ? extends Key> keyMapping) {
        return DataTool.toMap(list(mapper, wrapper, mapping), keyMapping);
    }

    /**
     * 查询数据并按照 keyMapping 规则转成 map 集合
     *
     * @param service    service
     * @param wrapper    wrapper
     * @param mapping    mapping
     * @param keyMapping keyMapping
     * @param <Entity>   数据实体泛型
     * @param <Mapper>   mapper泛型
     * @param <DTO>      DTO泛型
     * @param <Key>      key泛型
     * @return list
     */
    public static <Entity, Mapper extends BaseMapper<Entity>, DTO, Key> Map<Key, DTO> listMap(ServiceImpl<Mapper, Entity> service,
                                                                                              Wrapper<Entity> wrapper,
                                                                                              Function<? super Entity, ? extends DTO> mapping,
                                                                                              Function<? super DTO, ? extends Key> keyMapping) {
        return DataTool.toMap(list(service, wrapper, mapping), keyMapping);
    }

    /**
     * 查询数据并按照 keyMapping 规则转成 map 集合
     *
     * @param mapper                     mapper
     * @param lambdaQueryWrapperConsumer lambdaQueryWrapperConsumer
     * @param mapping                    mapping
     * @param keyMapping                 keyMapping
     * @param <Entity>                   数据实体泛型
     * @param <Mapper>                   mapper泛型
     * @param <DTO>                      DTO泛型
     * @param <Key>                      key泛型
     * @return list
     */
    public static <Entity, Mapper extends BaseMapper<Entity>, DTO, Key> Map<Key, DTO> list(Mapper mapper,
                                                                                           Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer,
                                                                                           Function<? super Entity, ? extends DTO> mapping,
                                                                                           Function<? super DTO, ? extends Key> keyMapping) {
        LambdaQueryWrapper<Entity> wrapper = Wrappers.<Entity>lambdaQuery();
        lambdaQueryWrapperConsumer.accept(wrapper);
        return DataTool.toMap(list(mapper, wrapper, mapping), keyMapping);
    }

    /**
     * 查询数据并按照 keyMapping 规则转成 map 集合
     *
     * @param service                    service
     * @param lambdaQueryWrapperConsumer lambdaQueryWrapperConsumer
     * @param mapping                    mapping
     * @param keyMapping                 keyMapping
     * @param <Entity>                   数据实体泛型
     * @param <Mapper>                   mapper泛型
     * @param <DTO>                      DTO泛型
     * @param <Key>                      key泛型
     * @return list
     */
    public static <Entity, Mapper extends BaseMapper<Entity>, DTO, Key> Map<Key, DTO> listMap(ServiceImpl<Mapper, Entity> service,
                                                                                              Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer,
                                                                                              Function<? super Entity, ? extends DTO> mapping,
                                                                                              Function<? super DTO, ? extends Key> keyMapping) {
        LambdaQueryWrapper<Entity> wrapper = Wrappers.<Entity>lambdaQuery();
        lambdaQueryWrapperConsumer.accept(wrapper);
        return DataTool.toMap(list(service, wrapper, mapping), keyMapping);
    }

    /**
     * 查询单条数据
     *
     * @param mapper   mapper
     * @param wrapper  wrapper
     * @param mapping  mapping
     * @param <Entity> 数据实体泛型
     * @param <Mapper> mapper泛型
     * @param <DTO>    DTO泛型
     * @return DTO实例
     */
    public static <Entity, Mapper extends BaseMapper<Entity>, DTO> DTO one(Mapper mapper,
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
     *
     * @param service  service
     * @param wrapper  wrapper
     * @param mapping  mapping
     * @param <Entity> 数据实体泛型
     * @param <Mapper> mapper泛型
     * @param <DTO>    DTO泛型
     * @return DTO实例
     */
    public static <Entity, Mapper extends BaseMapper<Entity>, DTO> DTO one(ServiceImpl<Mapper, Entity> service,
                                                                           Wrapper<Entity> wrapper,
                                                                           Function<? super Entity, ? extends DTO> mapping) {
        Entity entity = service.getOne(wrapper);
        if (isNull(entity)) {
            return null;
        }

        return mapping.apply(entity);
    }

    /**
     * 查询单条数据
     *
     * @param mapper                     mapper
     * @param lambdaQueryWrapperConsumer lambdaQueryWrapperConsumer
     * @param mapping                    mapping
     * @param <Entity>                   数据实体泛型
     * @param <Mapper>                   mapper泛型
     * @param <DTO>                      DTO泛型
     * @return DTO实例
     */
    public static <Entity, Mapper extends BaseMapper<Entity>, DTO> DTO one(Mapper mapper,
                                                                           Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer,
                                                                           Function<? super Entity, ? extends DTO> mapping) {
        LambdaQueryWrapper<Entity> wrapper = Wrappers.<Entity>lambdaQuery();
        lambdaQueryWrapperConsumer.accept(wrapper);
        return one(mapper, lambdaQueryWrapperConsumer, mapping);
    }

    /**
     * 查询单条数据
     *
     * @param service                    mapper
     * @param lambdaQueryWrapperConsumer lambdaQueryWrapperConsumer
     * @param mapping                    mapping
     * @param <Entity>                   数据实体泛型
     * @param <Mapper>                   mapper泛型
     * @param <DTO>                      DTO泛型
     * @return DTO实例
     */
    public static <Entity, Mapper extends BaseMapper<Entity>, DTO> DTO one(ServiceImpl<Mapper, Entity> service,
                                                                           Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer,
                                                                           Function<? super Entity, ? extends DTO> mapping) {
        LambdaQueryWrapper<Entity> wrapper = Wrappers.<Entity>lambdaQuery();
        lambdaQueryWrapperConsumer.accept(wrapper);
        return one(service, lambdaQueryWrapperConsumer, mapping);
    }

    /**
     * 查询单条数据
     *
     * @param mapper   mapper
     * @param wrapper  wrapper
     * @param <Entity> 数据实体泛型
     * @param <Mapper> mapper泛型
     * @return 数据实体
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> Entity one(Mapper mapper,
                                                                         Wrapper<Entity> wrapper) {
        Entity entity = mapper.selectOne(wrapper);
        if (isNull(entity)) {
            return null;
        }

        return entity;
    }

    /**
     * 查询单条数据
     *
     * @param service  service
     * @param wrapper  wrapper
     * @param <Entity> 数据实体泛型
     * @param <Mapper> mapper泛型
     * @return 数据实体
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> Entity one(ServiceImpl<Mapper, Entity> service,
                                                                         Wrapper<Entity> wrapper) {
        Entity entity = service.getOne(wrapper);
        if (isNull(entity)) {
            return null;
        }

        return entity;
    }

    /**
     * 分页查询数据
     *
     * @param mapper                     mapper
     * @param queryParam                 queryParam
     * @param lambdaQueryWrapperConsumer lambdaQueryWrapperConsumer
     * @param mapping                    mapping
     * @param <Entity>                   数据实体泛型
     * @param <DTO>                      DTO泛型
     * @param <Mapper>                   mapper泛型
     * @return PageRespVO
     */
    public static <Entity, DTO, Mapper extends BaseMapper<Entity>> RespVO<PageRespVO<DTO>> page(Mapper mapper,
                                                                                                PageQueryInterface queryParam,
                                                                                                Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer,
                                                                                                Function<Entity, DTO> mapping) {
        return PageRespVO.buildPageRespVO(pageEntity(mapper, queryParam, lambdaQueryWrapperConsumer), mapping);
    }

    /**
     * 条件分页查询数据
     *
     * @param service                    继承mybatis plus的ServiceImpl类的service
     * @param queryParam                 条件查询对象
     * @param lambdaQueryWrapperConsumer 条件查询wrapper接口
     * @param <E>                        数据库对象数据类型
     * @param <Param>                    条件查询对象类型
     * @param <Mapper>                   mybatis plus mapper类型
     * @param <Service>                  mybatis plus 继承ServiceImpl类的service类型
     * @return 分页结果集
     */
    public static <E, Param, Mapper extends BaseMapper<E>, Service extends ServiceImpl<Mapper, E>> RespVO<PageRespVO<E>>
    page(Service service,
         SimplePageQueryParam<Param> queryParam,
         Consumer<LambdaQueryWrapper<E>> lambdaQueryWrapperConsumer) {
        return page(service, queryParam.getPageQueryParam(), lambdaQueryWrapperConsumer);
    }

    /**
     * 条件分页查询数据
     *
     * @param mapper                     mybatis plus的Mapper接口
     * @param queryParam                 条件查询对象
     * @param lambdaQueryWrapperConsumer 条件查询wrapper接口
     * @param <E>                        数据库对象数据类型
     * @param <Param>                    条件查询对象类型
     * @param <Mapper>                   mybatis plus mapper类型
     * @return 分页结果集
     */
    public static <E, Param, Mapper extends BaseMapper<E>> RespVO<PageRespVO<E>>
    page(Mapper mapper,
         SimplePageQueryParam<Param> queryParam,
         Consumer<LambdaQueryWrapper<E>> lambdaQueryWrapperConsumer) {
        return page(mapper, queryParam.getPageQueryParam(), lambdaQueryWrapperConsumer);
    }

    /**
     * 条件分页查询数据
     *
     * @param service                    继承mybatis plus的ServiceImpl类的service
     * @param queryParam                 条件查询对象
     * @param lambdaQueryWrapperConsumer 条件查询wrapper接口
     * @param function                   数据实体转Vo对象
     * @param <V>                        分页结果集的数据类型
     * @param <E>                        数据库对象数据类型
     * @param <Param>                    条件查询对象类型
     * @param <Mapper>                   mybatis plus mapper类型
     * @param <Service>                  mybatis plus 继承ServiceImpl类的service类型
     * @return 分页结果集
     */
    public static <V, E, Param, Mapper extends BaseMapper<E>, Service extends ServiceImpl<Mapper, E>> RespVO<PageRespVO<V>>
    page(Service service,
         SimplePageQueryParam<Param> queryParam,
         Consumer<LambdaQueryWrapper<E>> lambdaQueryWrapperConsumer,
         Function<E, V> function) {
        return page(service, queryParam.getPageQueryParam(), lambdaQueryWrapperConsumer, function);
    }

    /**
     * 分页查询数据
     *
     * @param mapper                     mybatis plus的Mapper接口
     * @param queryParam                 条件查询对象
     * @param lambdaQueryWrapperConsumer 条件查询wrapper接口
     * @param function                   数据实体转Vo对象
     * @param <V>                        分页结果集的数据类型
     * @param <E>                        数据库对象数据类型
     * @param <Param>                    条件查询对象类型
     * @param <Mapper>                   mybatis plus mapper类型
     * @return 分页结果集
     */
    public static <V, E, Param, Mapper extends BaseMapper<E>> RespVO<PageRespVO<V>>
    page(Mapper mapper,
         SimplePageQueryParam<Param> queryParam,
         Consumer<LambdaQueryWrapper<E>> lambdaQueryWrapperConsumer,
         Function<E, V> function) {
        return page(mapper, queryParam.getPageQueryParam(), lambdaQueryWrapperConsumer, function);
    }

    /**
     * 分页查询数据
     *
     * @param service                    service
     * @param queryParam                 queryParam
     * @param lambdaQueryWrapperConsumer lambdaQueryWrapperConsumer
     * @param mapping                    mapping
     * @param <Entity>                   数据实体泛型
     * @param <DTO>                      DTO泛型
     * @param <Mapper>                   mapper泛型
     * @return PageRespVO
     */
    public static <Entity, DTO, Mapper extends BaseMapper<Entity>> RespVO<PageRespVO<DTO>> page(ServiceImpl<Mapper, Entity> service,
                                                                                                PageQueryInterface queryParam,
                                                                                                Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer,
                                                                                                Function<Entity, DTO> mapping) {
        if (Objects.isNull(mapping)) {
            throw new IllegalArgumentException("mapping不允许为空");
        }
        return PageRespVO.buildPageRespVO(pageEntity(service, queryParam, lambdaQueryWrapperConsumer), mapping);
    }

    /**
     * 分页查询数据
     *
     * @param mapper                     mapper
     * @param queryParam                 queryParam
     * @param lambdaQueryWrapperConsumer lambdaQueryWrapperConsumer
     * @param <Entity>                   数据实体泛型
     * @param <Mapper>                   mapper泛型
     * @return PageRespVO
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> RespVO<PageRespVO<Entity>> page(Mapper mapper,
                                                                                              PageQueryInterface queryParam,
                                                                                              Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer) {

        return PageRespVO.buildPageRespVO(pageEntity(mapper, queryParam, lambdaQueryWrapperConsumer));
    }

    /**
     * 分页查询数据
     *
     * @param service                    mapper
     * @param queryParam                 queryParam
     * @param lambdaQueryWrapperConsumer lambdaQueryWrapperConsumer
     * @param <Entity>                   数据实体泛型
     * @param <Mapper>                   mapper泛型
     * @return PageRespVO
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> RespVO<PageRespVO<Entity>> page(ServiceImpl<Mapper, Entity> service,
                                                                                              PageQueryInterface queryParam,
                                                                                              Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer) {

        return PageRespVO.buildPageRespVO(pageEntity(service, queryParam, lambdaQueryWrapperConsumer));
    }

    /**
     * 分页查询数据
     *
     * @param mapper                     mapper
     * @param queryParam                 queryParam
     * @param lambdaQueryWrapperConsumer lambdaQueryWrapperConsumer
     * @param <Entity>                   数据实体泛型
     * @param <Mapper>                   mapper泛型
     * @return Page
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> Page<Entity> pageEntity(Mapper mapper,
                                                                                      PageQueryInterface queryParam,
                                                                                      Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer) {
        Page<Entity> page = new Page<>(queryParam.getPage(), queryParam.getLimit());
        LambdaQueryWrapper<Entity> wrapper = Wrappers.<Entity>lambdaQuery();
        lambdaQueryWrapperConsumer.accept(wrapper);
        return mapper.selectPage(page, wrapper);
    }

    /**
     * 分页查询数据
     *
     * @param service                    service
     * @param queryParam                 queryParam
     * @param lambdaQueryWrapperConsumer lambdaQueryWrapperConsumer
     * @param <Entity>                   数据实体泛型
     * @param <Mapper>                   mapper泛型
     * @return Page
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> Page<Entity> pageEntity(ServiceImpl<Mapper, Entity> service,
                                                                                      PageQueryInterface queryParam,
                                                                                      Consumer<LambdaQueryWrapper<Entity>> lambdaQueryWrapperConsumer) {
        Page<Entity> page = new Page<>(queryParam.getPage(), queryParam.getLimit());
        LambdaQueryWrapper<Entity> wrapper = Wrappers.<Entity>lambdaQuery();
        lambdaQueryWrapperConsumer.accept(wrapper);
        return service.page(page, wrapper);
    }

    /**
     * 分页查询数据
     *
     * @param mapper     mapper
     * @param queryParam queryParam
     * @param wrapper    wrapper
     * @param <Entity>   数据实体泛型
     * @param <Mapper>   mapper泛型
     * @return PageRespVO
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> RespVO<PageRespVO<Entity>> page(Mapper mapper,
                                                                                              PageQueryInterface queryParam,
                                                                                              Wrapper<Entity> wrapper) {
        Page<Entity> page = new Page<>(queryParam.getPage(), queryParam.getLimit());
        IPage<Entity> pageResult = mapper.selectPage(page, wrapper);
        return PageRespVO.buildPageRespVO(pageResult);
    }

    /**
     * 分页查询数据
     *
     * @param service    service
     * @param queryParam queryParam
     * @param wrapper    wrapper
     * @param <Entity>   数据实体泛型
     * @param <Mapper>   mapper泛型
     * @return PageRespVO
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> RespVO<PageRespVO<Entity>> page(ServiceImpl<Mapper, Entity> service,
                                                                                              PageQueryInterface queryParam,
                                                                                              Wrapper<Entity> wrapper) {
        Page<Entity> page = new Page<>(queryParam.getPage(), queryParam.getLimit());
        IPage<Entity> pageResult = service.page(page, wrapper);
        return PageRespVO.buildPageRespVO(pageResult);
    }

    /**
     * 插入数据
     *
     * @param mapper   mapper
     * @param entity   entity
     * @param <Entity> 数据实体泛型
     * @param <Mapper> mapper泛型
     * @return 响应行数
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> int insert(Mapper mapper,
                                                                         Entity entity) {
        if (Objects.nonNull(entity)) {
            return mapper.insert(entity);
        }
        return -1;
    }

    /**
     * 插入数据
     *
     * @param service  service
     * @param entity   entity
     * @param <Entity> 数据实体泛型
     * @param <Mapper> mapper泛型
     * @return 响应行数
     */
    public static <Entity, Mapper extends BaseMapper<Entity>> boolean insert(ServiceImpl<Mapper, Entity> service,
                                                                             Entity entity) {
        if (Objects.nonNull(entity)) {
            return service.save(entity);
        }
        return true;
    }

    /**
     * 批量插入数据
     *
     * @param batchMapper batchMapper
     * @param entityList  entityList
     * @param <Mapper>    mapper泛型
     * @param <Entity>    数据实体泛型
     * @return 影响行数
     */
    public static <Mapper extends BaseMapper<Entity>, Entity> boolean batchInsert(ServiceImpl<Mapper, Entity> batchMapper,
                                                                                  List<Entity> entityList) {
        if (isEmpty(entityList)) {
            return true;
        }
        return batchMapper.saveBatch(entityList);
    }

    /**
     * 批量插入数据
     *
     * @param batchMapper batchMapper
     * @param mapping     mapping
     * @param dtoList     dtoList
     * @param <Mapper>    mapper泛型
     * @param <Entity>    数据实体泛型
     * @param <DTO>       DTO泛型
     * @return 是否成功
     */
    public static <Mapper extends BaseMapper<Entity>, Entity, DTO> boolean batchInsert(ServiceImpl<Mapper, Entity> batchMapper,
                                                                                       Function<List<DTO>, List<Entity>> mapping,
                                                                                       List<DTO> dtoList) {
        List<Entity> entityList = mapping.apply(dtoList);
        if (isEmpty(entityList)) {
            return true;
        }
        return batchMapper.saveBatch(entityList);
    }

    /**
     * 按照 entityIdFunction 更新数据
     *
     * @param mapper           mapper
     * @param entity           entity
     * @param entityIdFunction entityIdFunction
     * @param function         function
     * @param <Mapper>         mapper泛型
     * @param <Entity>         数据实体泛型
     * @param <K>              key泛型
     * @return 影响行数
     */
    public static <Mapper extends BaseMapper<Entity>, Entity, K extends Serializable> int update(Mapper mapper,
                                                                                                 Entity entity,
                                                                                                 Function<Entity, K> entityIdFunction,
                                                                                                 Function<Entity, Entity> function) {
        Entity entityFromDB = mapper.selectById(entityIdFunction.apply(entity));
        if (isNull(entityFromDB)) {
            return 0;
        }
        return mapper.updateById(function.apply(entityFromDB));
    }

}
