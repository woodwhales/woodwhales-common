package cn.woodwhales.common.webhook.model.param;

import cn.woodwhales.common.webhook.model.request.BaseWebhookRequestBody;
import org.apache.commons.lang3.StringUtils;

/**
 * 请求参数对象
 * @author woodwhales on 2021-07-16 21:38
 */
public class ExecuteParam {

    /**
     * 通知地址
     */
    private String url;

    /**
     * 签名密钥
     */
    private String secret;

    /**
     * 通知内容
     */
    BaseWebhookRequestBody requestBody;

    private ExecuteParam(String url, String secret, BaseWebhookRequestBody requestBody) {
        this.url = url;
        this.secret = secret;
        this.requestBody = requestBody;
    }

    public static ExecuteParam newInstance(String url, String secret, BaseWebhookRequestBody requestBody) {
        return new ExecuteParam(url, secret, requestBody);
    }

    public String getNoticeUrl() {
        if(StringUtils.isBlank(this.secret)) {
            return this.url;
        } else {
            return requestBody.getUrlAndSignContent(this);
        }
    }

    public String getNoticeContent() {
        return this.requestBody.toJsonSting();
    }

    public String getUrl() {
        return url;
    }

    public String getSecret() {
        return secret;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BaseWebhookRequestBody getRequestBody() {
        return requestBody;
    }
}
