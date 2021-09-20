package cn.woodwhales.common.webhook.event;

import cn.woodwhales.common.webhook.enums.WebhookProductEnum;
import cn.woodwhales.common.webhook.model.request.BaseWebhookRequestBody;

import java.util.function.Consumer;

/**
 * webhook 事件对象创建工厂
 * @author woodwhales on 2021-09-15 15:17
 */
public class WebhookEventFactory {

    private static WebhookEvent newWebhookEvent(WebhookProductEnum webhookProductEnum,
                                                Object source,
                                                String title,
                                                Throwable throwable,
                                                Consumer<BaseWebhookRequestBody> consumer) {
        return new WebhookEvent(source, throwable, webhookProductEnum, title, consumer);
    }

    public static WebhookEvent newWebhookEvent(Object source,
                                                String title,
                                                Throwable throwable,
                                                Consumer<BaseWebhookRequestBody> consumer) {
        return new WebhookEvent(source, throwable, null, title, consumer);
    }

    public static WebhookEvent feiShu(Object source,
                                      String title,
                                      Throwable throwable,
                                      Consumer<BaseWebhookRequestBody> consumer) {
        return newWebhookEvent(WebhookProductEnum.FEI_SHU, source, title, throwable, consumer);
    }

    public static WebhookEvent weCom(Object source,
                                      String title,
                                      Throwable throwable,
                                      Consumer<BaseWebhookRequestBody> consumer) {
        return newWebhookEvent(WebhookProductEnum.WE_COM, source, title, throwable, consumer);
    }

    public static WebhookEvent dingTalk(Object source,
                                      String title,
                                      Throwable throwable,
                                      Consumer<BaseWebhookRequestBody> consumer) {
        return newWebhookEvent(WebhookProductEnum.DING_TALK, source, title, throwable, consumer);
    }

}
