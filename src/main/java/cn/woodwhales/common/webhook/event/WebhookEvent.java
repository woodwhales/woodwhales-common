package cn.woodwhales.common.webhook.event;

import cn.woodwhales.common.webhook.enums.WebhookProductEnum;
import cn.woodwhales.common.webhook.model.WebhookGlobalInfo;
import cn.woodwhales.common.webhook.model.request.BaseWebhookRequestBody;
import cn.woodwhales.common.webhook.model.request.WebhookRequestBodyFactory;
import org.springframework.context.ApplicationEvent;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * WebhookEvent spring 事件对象
 * @author woodwhales on 2021-09-15 12:45
 */
public class WebhookEvent extends ApplicationEvent {

    private WebhookGlobalInfo webhookGlobalInfo;

    private String title;

    private BaseWebhookRequestBody baseWebhookRequestBody;

    private WebhookProductEnum webhookProductEnum;

    private Consumer<BaseWebhookRequestBody> consumer;

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
                        WebhookProductEnum webhookProductEnum,
                        String title,
                        Consumer<BaseWebhookRequestBody> consumer) {
        super(source);
        new WebhookEvent(source, throwable, webhookProductEnum, title, consumer, null, null);
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
        this.baseWebhookRequestBody = WebhookRequestBodyFactory.newInstance(webhookProductEnum, title);
        consumer.accept(this.baseWebhookRequestBody);
        this.webhookGlobalInfo = new WebhookGlobalInfo(webhookProductEnum, this.throwable, null);
        this.baseWebhookRequestBody.addGlobalInfo(this.webhookGlobalInfo);
        return this;
    }

    public boolean needFillField() {
        return Objects.isNull(this.webhookProductEnum);
    }

    public WebhookProductEnum getWebhookProductEnum() {
        return webhookProductEnum;
    }

    public void setMachineInfoMap(LinkedHashMap<String, String> machineInfoMap) {
        this.webhookGlobalInfo.setMachineInfoMap(machineInfoMap);
    }

    public void setGitProperties(Properties gitProperties) {
        this.webhookGlobalInfo.setGitProperties(gitProperties);
    }

    public String getOccurTime() {
        return this.webhookGlobalInfo.getOccurTime();
    }

    public BaseWebhookRequestBody getBaseWebhookRequestBody() {
        return baseWebhookRequestBody;
    }

    public String getTitle() {
        return title;
    }

    public void setBasePackName(String basePackName) {
        this.webhookGlobalInfo.setBasePackName(basePackName);
    }
}
