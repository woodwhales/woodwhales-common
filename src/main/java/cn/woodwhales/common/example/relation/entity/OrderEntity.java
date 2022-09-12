package cn.woodwhales.common.example.relation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author woodwhales on 2022-09-12 17:41
 */
@Data
@TableName("order")
public class OrderEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("order_name")
    private String orderName;

}
