package cn.woodwhales.common.webhook.model.request;

import cn.woodwhales.common.webhook.enums.WebhookProductEnum;

import java.util.List;

/**
 * @author woodwhales on 2021-07-20 10:02
 */
public class WebhookRequestBodyFactory {

    public static BaseWebhookRequestBody newInstance(WebhookProductEnum webhookProductEnum,
                                                     String title) {
        return newInstance(webhookProductEnum, title, null, null);
    }

    public static BaseWebhookRequestBody newInstance(WebhookProductEnum webhookProductEnum,
                                                     String title,
                                                     List<String> userIdList,
                                                     List<String> userMobileList) {
        BaseWebhookRequestBody requestBody = null;
        switch (webhookProductEnum) {
            case WE_COM :
                requestBody = WeComRequestBody.newInstance(title);
                requestBody.addUserMobileList(userMobileList);
                requestBody.addUserIdList(userIdList);
                break;

            case DING_TALK :
                requestBody = DingTalkRequestBody.newInstance(title);
                requestBody.addUserMobileList(userMobileList);
                requestBody.addUserIdList(userIdList);
                break;

            case FEI_SHU :
                requestBody = FeiShuRequestBody.newInstance(title);
                requestBody.addUserIdList(userIdList);
                break;
        }
        requestBody.setWebhookProductEnum(webhookProductEnum);
        return requestBody;
    }

}
