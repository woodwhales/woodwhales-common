package cn.woodwhales.common.example.relation.mapper;

import cn.woodwhales.common.example.relation.entity.UserRoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author woodwhales on 2022-09-09 17:08
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleEntity>  {
}
