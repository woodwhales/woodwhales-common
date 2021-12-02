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
     * 基础包全类名
     */
    private String basePackageName;

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

    /**
     * 创建 WebhookEvent
     * @param source source
     * @param throwable throwable
     * @param webhookProductEnum webhookProductEnum
     * @param title title
     * @param consumer consumer
     */
    public WebhookEvent(Object source,
                        Throwable throwable,
                        WebhookProductEnum webhookProductEnum,
                        String title,
                        Consumer<BaseWebhookRequestBody> consumer) {
        super(source);
        this.title = title;
        this.consumer = consumer;
        this.throwable = throwable;
        if (Objects.nonNull(webhookProductEnum)) {
            fillField(webhookProductEnum);
        }
    }

    public WebhookEvent(Object source,
                        Throwable throwable,
                        WebhookProductEnum webhookProductEnum,
                        String title,
                        List<String> userIdList,
                        Consumer<BaseWebhookRequestBody> consumer) {
        super(source);
        this.title = title;
        this.consumer = consumer;
        this.throwable = throwable;
        if(userIdList != null && userIdList.size() > 0) {
            this.userIdList = userIdList;
        }
        if (Objects.nonNull(webhookProductEnum)) {
            fillField(webhookProductEnum);
        }
    }

    public WebhookEvent(Object source,
                        Throwable throwable,
                        WebhookProductEnum webhookProductEnum,
                        String title,
                        Consumer<BaseWebhookRequestBody> consumer,
                        List<String> userMobileList) {
        super(source);
        this.title = title;
        this.consumer = consumer;
        this.throwable = throwable;
        if(userMobileList != null && userMobileList.size() > 0) {
            this.userMobileList = userMobileList;
        }
        if (Objects.nonNull(webhookProductEnum)) {
            fillField(webhookProductEnum);
        }
    }

    public WebhookEvent(Object source,
                        Throwable throwable,
                        WebhookProductEnum webhookProductEnum,
                        String title,
                        Consumer<BaseWebhookRequestBody> consumer,
                        List<String> userIdList,
                        List<String> userMobileList) {
        super(source);
        this.title = title;
        this.consumer = consumer;
        this.throwable = throwable;
        if(userIdList != null && userIdList.size() > 0) {
            this.userIdList = userIdList;
        }
        if(userMobileList != null && userMobileList.size() > 0) {
            this.userMobileList = userMobileList;
        }
        if (Objects.nonNull(webhookProductEnum)) {
            fillField(webhookProductEnum);
        }
    }

    public WebhookEvent fillField(WebhookProductEnum webhookProductEnum) {
        this.webhookProductEnum = webhookProductEnum;
        this.baseWebhookRequestBody =
                WebhookRequestBodyFactory.newInstance(webhookProductEnum, title, this.userIdList, this.userMobileList);
        this.consumer.accept(this.baseWebhookRequestBody);
        this.globalInfo = new GlobalInfo(webhookProductEnum, this.throwable, null);
        this.baseWebhookRequestBody.addGlobalInfo(this.globalInfo);
        return this;
    }

    /**
     * 是否需要动态获取 WebhookProductEnum 枚举
     * @return 是否需要动态获取 WebhookProductEnum 枚举
     */
    public boolean needFillField() {
        return Objects.isNull(this.webhookProductEnum);
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

    public void setBasePackName(String basePackName) {
        this.globalInfo.setBasePackName(basePackName);
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }

    public String getBasePackageName() {
        return basePackageName;
    }

    public void setBasePackageName(String basePackageName) {
        this.basePackageName = basePackageName;
    }
}
