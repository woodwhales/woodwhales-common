package cn.woodwhales.common.webhook.event;

import cn.woodwhales.common.webhook.enums.WebhookProductEnum;
import cn.woodwhales.common.webhook.model.GlobalInfo;
import cn.woodwhales.common.webhook.model.request.BaseWebhookRequestBody;
import cn.woodwhales.common.webhook.model.request.WebhookRequestBodyFactory;
import org.springframework.context.ApplicationEvent;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * @author woodwhales on 2021-09-15 12:45
 */
public class WebhookEvent extends ApplicationEvent {

    /**
     * webhook 通知链接地址
     */
    private String noticeUrl;

    /**
     * webhook 密钥
     */
    private String secret;

    /**
     * 基础包全类名
     */
    private String[] basePackageNames;

    /**
     * 全局信息
     */
    private GlobalInfo globalInfo;

    /**
     * 消息主题
     */
    private String title;

    /**
     * 请求报文对象
     */
    private BaseWebhookRequestBody baseWebhookRequestBody;

    /**
     * WebhookProductEnum 枚举
     */
    private WebhookProductEnum webhookProductEnum;

    /**
     * 请求报文对象 Consumer 接口
     */
    private Consumer<BaseWebhookRequestBody> consumer;

    /**
     * 异常对象
     */
    private Throwable throwable;

    /**
     * 用户id集合
     */
    private List<String> userIdList;

    /**
     * 用户手机号集合
     */
    private List<String> userMobileList;

    public WebhookEvent(Object source,
                        Throwable throwable,
                        String[] basePackageNames,
                        String title,
                        String noticeUrl,
                        String secret,
                        Consumer<BaseWebhookRequestBody> consumer,
                        List<String> userIdList,
                        List<String> userMobileList) {
        super(source);
        this.title = title;
        this.noticeUrl = noticeUrl;
        this.secret = secret;
        this.consumer = consumer;
        this.throwable = throwable;
        this.basePackageNames = basePackageNames;
        if (userIdList != null && userIdList.size() > 0) {
            this.userIdList = userIdList;
        }
        if (userMobileList != null && userMobileList.size() > 0) {
            this.userMobileList = userMobileList;
        }
        this.fillField();
        if (Objects.nonNull(this.baseWebhookRequestBody) && Objects.nonNull(this.consumer)) {
            this.consumer.accept(this.baseWebhookRequestBody);
        }
    }

    private void fillField() {
        if (Objects.nonNull(this.noticeUrl)) {
            this.webhookProductEnum = WebhookProductEnum.getWebhookProductEnumByNoticeUrl(this.noticeUrl);
            this.globalInfo = new GlobalInfo(this.webhookProductEnum, this.throwable, this.basePackageNames, null);
            this.baseWebhookRequestBody = WebhookRequestBodyFactory.newInstance(this.webhookProductEnum, this.title, this.consumer, this.getUserIdList(), this.userMobileList);
        }
    }

    public static class Builder {
        private Object source;
        private String title;
        private Throwable throwable;
        private String[] basePackageNames;
        private String noticeUrl;
        private String secret;
        private Consumer<BaseWebhookRequestBody> consumer;
        private List<String> userIdList;
        private List<String> userMobileList;

        public static WebhookEvent.Builder build(Object source, String title) {
            final WebhookEvent.Builder builder = new WebhookEvent.Builder();
            builder.source = source;
            builder.title = title;
            return builder;
        }

        public WebhookEvent.Builder throwable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        public WebhookEvent.Builder throwable(Throwable throwable, String... basePackageNames) {
            this.throwable = throwable;
            this.basePackageNames = basePackageNames;
            return this;
        }

        public WebhookEvent.Builder basePackageNames(String... basePackageNames) {
            this.basePackageNames = basePackageNames;
            return this;
        }

        public WebhookEvent.Builder consumer(Consumer<BaseWebhookRequestBody> consumer) {
            this.consumer = consumer;
            return this;
        }

        public WebhookEvent.Builder userIdList(List<String> userIdList) {
            this.userIdList = userIdList;
            return this;
        }

        public WebhookEvent.Builder userMobileList(List<String> userMobileList) {
            this.userMobileList = userMobileList;
            return this;
        }

        public WebhookEvent.Builder noticeUrl(String noticeUrl) {
            this.noticeUrl = noticeUrl;
            return this;
        }

        public WebhookEvent.Builder noticeUrl(String noticeUrl, String secret) {
            this.noticeUrl = noticeUrl;
            this.secret = secret;
            return this;
        }

        public WebhookEvent build() {
            return new WebhookEvent(this.source, this.throwable, this.basePackageNames,
                    this.title, this.noticeUrl, this.secret,
                    this.consumer, this.userIdList, this.userMobileList);
        }
    }

    public WebhookProductEnum getWebhookProductEnum() {
        return webhookProductEnum;
    }

    public void setMachineInfoMap(LinkedHashMap<String, String> machineInfoMap) {
        this.globalInfo.setMachineInfoMap(machineInfoMap);
    }

    public void setGitProperties(Properties gitProperties) {
        this.globalInfo.setGitProperties(gitProperties);
    }

    public String getOccurTime() {
        return this.globalInfo.getOccurTime();
    }

    public BaseWebhookRequestBody getBaseWebhookRequestBody() {
        return baseWebhookRequestBody;
    }

    public String getTitle() {
        return title;
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public String getSecret() {
        return secret;
    }

    public String[] getBasePackageNames() {
        return basePackageNames;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public List<String> getUserIdList() {
        return userIdList;
    }

    public List<String> getUserMobileList() {
        return userMobileList;
    }
}
