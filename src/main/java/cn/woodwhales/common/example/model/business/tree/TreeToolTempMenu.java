package cn.woodwhales.common.example.model.business.tree;

/**
 * @author woodwhales on 2021-07-25 12:26
 */
public class TreeToolTempMenu {
    private Integer id;
    private Integer parentId;
    private String cityName;
    private Integer sort;

    public TreeToolTempMenu(Integer id, Integer parentId, String cityName, Integer sort) {
        this.id = id;
        this.parentId = parentId;
        this.cityName = cityName;
        this.sort = sort;
    }

    public Integer getId() {
        return id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public String getCityName() {
        return cityName;
    }

    public Integer getSort() {
        return sort;
    }
}
