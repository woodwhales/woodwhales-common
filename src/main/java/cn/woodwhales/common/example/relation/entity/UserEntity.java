package cn.woodwhales.common.example.relation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author woodwhales on 2022-09-09 17:04
 */
@Data
@TableName("user")
public class UserEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_name")
    private String userName;

}
