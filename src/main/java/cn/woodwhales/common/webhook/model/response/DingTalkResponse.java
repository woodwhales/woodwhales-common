package cn.woodwhales.common.webhook.model.response;

/**
 * 钉钉 webhook 响应对象
 * @author woodwhales 2021-07-19 14:38
 */
public class DingTalkResponse {

    private Integer errcode;
    private String errmsg;

    public DingTalkResponse() {
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