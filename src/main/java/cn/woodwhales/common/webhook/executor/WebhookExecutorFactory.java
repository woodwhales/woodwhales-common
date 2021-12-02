package cn.woodwhales.common.webhook.executor;


import cn.woodwhales.common.webhook.enums.WebhookProductEnum;
import cn.woodwhales.common.webhook.model.request.BaseWebhookRequestBody;
import cn.woodwhales.common.webhook.model.request.WebhookRequestBodyFactory;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author woodwhales on 2021-07-20 9:26
 */
public class WebhookExecutorFactory {

    public static <RequestBody extends BaseWebhookRequestBody> BaseWebhookExecutor newInstance(WebhookProductEnum webhookProductEnum) {
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
     * @param requestBody 请求报文
     */
    public static void execute(String url, BaseWebhookRequestBody requestBody) {
        newInstance(requestBody.getWebhookProductEnum()).execute(url, requestBody);
    }

    /**
     * 请求执行
     * @param webhookProductEnum webhook 类型枚举
     * @param url 请求地址
     * @param title 报文标题
     * @param consumer consumer
     */
    public static void execute(WebhookProductEnum webhookProductEnum,
                               String url,
                               String title,
                               Consumer<BaseWebhookRequestBody> consumer) {
        execute(webhookProductEnum, url, title, consumer, null, null);
    }

    public static void execute(WebhookProductEnum webhookProductEnum,
                               String url,
                               String title,
                               Consumer<BaseWebhookRequestBody> consumer,
                               List<String> userIdList,
                               List<String> userMobileList) {
        BaseWebhookRequestBody requestBody = WebhookRequestBodyFactory.newInstance(webhookProductEnum, title, userIdList, userMobileList);
        consumer.accept(requestBody);
        newInstance(requestBody.getWebhookProductEnum()).execute(url, requestBody);
    }
}
