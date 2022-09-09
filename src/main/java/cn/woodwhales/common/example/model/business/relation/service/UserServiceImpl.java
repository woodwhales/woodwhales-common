package cn.woodwhales.common.example.model.business.relation.service;

import cn.woodwhales.common.example.model.business.relation.RoleEntity;
import cn.woodwhales.common.example.model.business.relation.UserEntity;
import cn.woodwhales.common.example.model.business.relation.UserRoleEntity;
import cn.woodwhales.common.example.model.business.relation.mapper.UserMapper;
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

}
