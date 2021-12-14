package cn.woodwhales.common.webhook.executor;


import cn.woodwhales.common.webhook.enums.WebhookProductEnum;
import cn.woodwhales.common.webhook.model.GlobalInfo;
import cn.woodwhales.common.webhook.model.request.BaseWebhookRequestBody;
import cn.woodwhales.common.webhook.model.request.WebhookRequestBodyFactory;
import cn.woodwhales.common.webhook.plugin.WebhookExtraInfo;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author woodwhales on 2021-07-20 9:26
 */
public class WebhookExecutorFactory {

    private static <RequestBody extends BaseWebhookRequestBody> BaseWebhookExecutor newInstance(WebhookProductEnum webhookProductEnum) {
        BaseWebhookExecutor baseWebhookExecutor = null;
        switch (webhookProductEnum) {
            case WE_COM :
                baseWebhookExecutor = WeComWebhookExecutor.<RequestBody>newInstance();
                break;

            case DING_TALK :
                baseWebhookExecutor = DingTalkWebhookExecutor.<RequestBody>newInstance();
                break;

            case FEI_SHU :
                baseWebhookExecutor = FeiShuWebhookExecutor.<RequestBody>newInstance();
                break;
        }
        return baseWebhookExecutor;
    }

    /**
     * 请求执行
     * @param url 请求地址
     * @param secret 签名密钥
     * @param requestBody 请求报文
     */
    private static void execute(String url, String secret, BaseWebhookRequestBody requestBody) {
        WebhookExecutorFactory.newInstance(requestBody.getWebhookProductEnum()).execute(url, secret, requestBody);
    }

    private static void execute(WebhookProductEnum webhookProductEnum,
                               String url,
                               String title,
                               Consumer<BaseWebhookRequestBody> consumer,
                               List<String> userIdList,
                               List<String> userMobileList) {
        BaseWebhookRequestBody requestBody = WebhookRequestBodyFactory.newInstance(webhookProductEnum, title, consumer, userIdList, userMobileList);
        newInstance(requestBody.getWebhookProductEnum()).execute(url, requestBody);
    }

    public static class Builder {
        /**
         * 通知地址
         * 必选
         */
        private String url;

        /**
         * 标题
         * 必选
         */
        private String title;

        /**
         * 必选
         */
        private Consumer<BaseWebhookRequestBody> consumer;

        /**
         * 可选
         */
        private BaseWebhookRequestBody baseWebhookRequestBody;

        /**
         * 异常对象
         * 可选
         */
        private Throwable throwable;

        /**
         * 基础包类名
         * 可选
         */
        private String[] basePackageNames;

        /**
         * 密钥
         * 可选
         */
        private String secret;

        /**
         * 用户id集合
         */
        private List<String> userIdList;

        /**
         * 手机号集合
         */
        private List<String> userMobileList;

        /**
         * 扩展信息
         */
        private WebhookExtraInfo webhookExtraInfo;

        public static Builder build(String url, String title, Consumer<BaseWebhookRequestBody> consumer) {
            Builder builder = new Builder();
            builder.url = url;
            builder.title = title;
            builder.consumer = consumer;
            return builder;
        }

        public static Builder build(String url, String title, BaseWebhookRequestBody baseWebhookRequestBody) {
            Builder builder = new Builder();
            builder.url = url;
            builder.title = title;
            builder.baseWebhookRequestBody = baseWebhookRequestBody;
            return builder;
        }

        public Builder throwable(Throwable throwable, String ...basePackageNames) {
            this.throwable = throwable;
            this.basePackageNames = basePackageNames;
            return this;
        }

        public Builder throwable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        public Builder basePackageNames(String ...basePackageNames) {
            this.basePackageNames = basePackageNames;
            return this;
        }

        public Builder secret(String secret) {
            this.secret = secret;
            return this;
        }

        public Builder userIdList(List<String> userIdList) {
            this.userIdList = userIdList;
            return this;
        }

        public Builder userMobileList(List<String> userMobileList) {
            this.userMobileList = userMobileList;
            return this;
        }

        public Builder baseWebhookRequestBody(BaseWebhookRequestBody baseWebhookRequestBody) {
            this.baseWebhookRequestBody = baseWebhookRequestBody;
            return this;
        }

        public Builder webhookExtraInfo(WebhookExtraInfo webhookExtraInfo) {
            this.webhookExtraInfo = webhookExtraInfo;
            return this;
        }

        public Builder webhookExtraInfo(long duration, TimeUnit unit) {
            this.webhookExtraInfo = new WebhookExtraInfo(duration, unit);
            return this;
        }

        public void execute() {
            if(Objects.isNull(this.baseWebhookRequestBody)) {
                this.baseWebhookRequestBody = WebhookRequestBodyFactory.newInstance(WebhookProductEnum.getWebhookProductEnumByNoticeUrl(this.url), this.title, this.consumer, this.userIdList, this.userMobileList);
            }

            BaseWebhookRequestBody requestBody = this.baseWebhookRequestBody;
            final WebhookProductEnum webhookProductEnum = requestBody.getWebhookProductEnum();
            requestBody.addGlobalInfo(new GlobalInfo(webhookProductEnum, this.throwable, this.basePackageNames, this.webhookExtraInfo));
            WebhookExecutorFactory.newInstance(webhookProductEnum).execute(url, secret, requestBody);
        }
    }

}
