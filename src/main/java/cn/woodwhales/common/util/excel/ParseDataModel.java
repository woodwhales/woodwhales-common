package cn.woodwhales.common.util.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author woodwhales on 2022-07-08 0:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParseDataModel {

    /**
     * 当前行数
     */
    public Integer rowIndex;

    /**
     * 最大列长度
     */
    public Integer maxCellNumber;

}
