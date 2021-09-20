package cn.woodwhales.common.webhook.model.request;

import cn.woodwhales.common.webhook.enums.WebhookProductEnum;

/**
 * webhook 请求对象创建工厂
 * @author woodwhales on 2021-07-20 10:02
 */
public class WebhookRequestBodyFactory {

    public static BaseWebhookRequestBody newInstance(WebhookProductEnum webhookProductEnum, String title) {
        BaseWebhookRequestBody requestBody = null;
        switch (webhookProductEnum) {
            case WE_COM :
                requestBody = WeComRequestBody.newInstance(title);
                break;

            case DING_TALK :
                requestBody = DingTalkRequestBody.newInstance(title);
                break;

            case FEI_SHU :
                requestBody = FeiShuRequestBody.newInstance(title);
                break;
        }
        requestBody.setWebhookProductEnum(webhookProductEnum);
        return requestBody;
    }

}
