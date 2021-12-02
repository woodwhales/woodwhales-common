package cn.woodwhales.common.webhook.model.response;

/**
 * 飞书webhook响应对象
 * @author woodwhales 2021-07-19 14:38
 */
public class FeiShuResponse {
    private Integer code;
    private String msg;

    private Object Extra;
    private Integer StatusCode;
    private String StatusMessage;

    public FeiShuResponse() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getExtra() {
        return Extra;
    }

    public void setExtra(Object extra) {
        Extra = extra;
    }

    public Integer getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(Integer statusCode) {
        StatusCode = statusCode;
    }

    public String getStatusMessage() {
        return StatusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        StatusMessage = statusMessage;
    }
}