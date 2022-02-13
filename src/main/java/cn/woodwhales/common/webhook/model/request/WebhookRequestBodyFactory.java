package cn.woodwhales.common.webhook.model.request;

import cn.woodwhales.common.webhook.enums.WebhookProductEnum;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author woodwhales on 2021-07-20 10:02
 */
public class WebhookRequestBodyFactory {

    public static BaseWebhookRequestBody newInstance(WebhookProductEnum webhookProductEnum,
                                                     String title,
                                                     Consumer<BaseWebhookRequestBody> consumer,
                                                     List<String> userIdList,
                                                     List<String> userMobileList) {
        BaseWebhookRequestBody requestBody = null;
        switch (webhookProductEnum) {
            case WE_COM:
                requestBody = WeComRequestBody.newInstance(title);
                requestBody.addUserMobileList(userMobileList);
                requestBody.addUserIdList(userIdList);
                break;

            case DING_TALK:
                requestBody = DingTalkRequestBody.newInstance(title);
                requestBody.addUserMobileList(userMobileList);
                requestBody.addUserIdList(userIdList);
                break;

            case FEI_SHU:
                requestBody = FeiShuRequestBody.newInstance(title);
                requestBody.addUserIdList(userIdList);
                break;
        }
        requestBody.setWebhookProductEnum(webhookProductEnum);
        if (Objects.nonNull(consumer)) {
            consumer.accept(requestBody);
        }
        return requestBody;
    }

}
