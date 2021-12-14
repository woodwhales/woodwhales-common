package cn.woodwhales.common.webhook.event;

import cn.woodwhales.common.webhook.enums.WebhookProductEnum;
import cn.woodwhales.common.webhook.model.request.BaseWebhookRequestBody;

import java.util.List;
import java.util.function.Consumer;

/**
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

    public static WebhookEvent newWebhookEventWithUserId(Object source,
                                                         String title,
                                                         Throwable throwable,
                                                         Consumer<BaseWebhookRequestBody> consumer,
                                                         List<String> userIdList) {
        return new WebhookEvent(source, throwable, null, title, userIdList, consumer);
    }

    public static WebhookEvent newWebhookEventWithUserMobile(Object source,
                                                             String title,
                                                             Throwable throwable,
                                                             Consumer<BaseWebhookRequestBody> consumer,
                                                             List<String> userMobileList) {
        return new WebhookEvent(source, throwable, null, title, consumer, userMobileList);
    }

    public static WebhookEvent newWebhookEvent(Object source,
                                               String title,
                                               Throwable throwable,
                                               Consumer<BaseWebhookRequestBody> consumer,
                                               List<String> userIdList,
                                               List<String> userMobileList) {
        return new WebhookEvent(source, throwable, null, title, consumer, userIdList, userMobileList);
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
