package cn.woodwhales.common.example.relation.service;

import cn.woodwhales.common.example.relation.entity.OrderEntity;
import cn.woodwhales.common.example.relation.entity.RoleEntity;
import cn.woodwhales.common.example.relation.entity.UserEntity;
import cn.woodwhales.common.example.relation.entity.UserRoleEntity;
import cn.woodwhales.common.example.relation.mapper.UserMapper;
import cn.woodwhales.common.mybatisplus.MybatisPlusRelationExecutor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author woodwhales on 2022-09-09 17:09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> {

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Autowired
    private OrderServiceImpl orderService;

    public List<RoleEntity> getRoleListByUserId(Long userId) {
        return MybatisPlusRelationExecutor.executeQuery(userId,
                                                this,
                                                        UserEntity::getId,
                                                        userRoleService,
                                                        UserRoleEntity::getUserId,
                                                        UserRoleEntity::getRoleId,
                                                        roleService,
                                                        RoleEntity::getId);
    }

    public List<OrderEntity> getOrderListByUserId(Long userId) {
        return MybatisPlusRelationExecutor.executeQuery(userId, this, UserEntity::getId, orderService,
                                                                            OrderEntity::getUserId);
    }

}
