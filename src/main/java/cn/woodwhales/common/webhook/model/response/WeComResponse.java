package cn.woodwhales.common.webhook.model.response;

/**
 * 企业微信webhook响应对象
 * @author woodwhales 2021-07-19 14:38
 */
public class WeComResponse {

    private Integer errcode;
    private String errmsg;

    public WeComResponse() {
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
