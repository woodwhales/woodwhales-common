package cn.woodwhales.common.example.relation.service;

import cn.woodwhales.common.example.relation.entity.OrderEntity;
import cn.woodwhales.common.example.relation.mapper.OrderMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author woodwhales on 2022-09-12 17:43
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> {
}
