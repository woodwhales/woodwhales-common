package cn.woodwhales.common.webhook.model.param;

/**
 * webhook 请求参数对象
 * @author woodwhales on 2021-07-16 21:38
 */
public class WebhookExecuteParam {
    /**
     * 通知地址
     */
    public String url;

    /**
     * 通知内容
     */
    public String content;

    private WebhookExecuteParam(String url, String content) {
        this.url = url;
        this.content = content;
    }

    public static WebhookExecuteParam newInstance(String url, String content) {
        return new WebhookExecuteParam(url, content);
    }
}
