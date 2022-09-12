package cn.woodwhales.common.example.relation.mapper;

import cn.woodwhales.common.example.relation.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author woodwhales on 2022-09-12 17:42
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderEntity> {
}
