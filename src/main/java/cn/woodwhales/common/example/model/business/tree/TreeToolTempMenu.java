package cn.woodwhales.common.example.model.business.tree;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author woodwhales on 2021-07-25 12:26
 */
@Data
@Accessors(chain = true)
public class TreeToolTempMenu {
    private Integer id;
    private Integer parentId;
    private String cityName;
    private Integer sort;

    /**
     * 备注信息
     */
    private String memo;

    /**
     * 扩展信息1
     */
    private String extraInfo1;

    /**
     * 扩展信息2
     */
    private String extraInfo2;

    public TreeToolTempMenu(Integer id, Integer parentId, String cityName, Integer sort, String memo) {
        this.id = id;
        this.parentId = parentId;
        this.cityName = cityName;
        this.sort = sort;
        this.memo = memo;
    }

}
