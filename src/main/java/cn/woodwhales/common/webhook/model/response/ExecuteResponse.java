package cn.woodwhales.common.webhook.model.response;

/**
 * 通用 webhook 响应对象
 * @author woodwhales on 2021-07-19 9:38
 */
public class ExecuteResponse<R> {

    /**
     * 响应原始报文
     */
    public String originResponseContent;

    /**
     * 原始报文解析之后的数据对象
     */
    public R parsedResponseObject;

    /**
     * 校验响应结果
     */
    public boolean checkResult;

    /**
     * http 响应状态码
     */
    public int statusCode;

    public ExecuteResponse(int statusCode, String originResponseContent) {
        this.statusCode = statusCode;
        this.originResponseContent = originResponseContent;
    }

}
