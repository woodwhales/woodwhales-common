package cn.woodwhales.common.webhook.executor;

import cn.woodwhales.common.webhook.enums.WebhookProductEnum;
import cn.woodwhales.common.webhook.model.request.BaseWebhookRequestBody;
import cn.woodwhales.common.webhook.model.request.WeComRequestBody;
import cn.woodwhales.common.webhook.model.response.WeComResponse;
import cn.woodwhales.common.webhook.model.response.ExecuteResponse;

import java.util.Objects;

/**
 * @author woodwhales on 2021-07-19 11:32
 */
public class WeComWebhookExecutor<RequestBody extends BaseWebhookRequestBody> extends BaseWebhookExecutor<WeComRequestBody, WeComResponse> {

    private static final int ERR_CODE_SUCCESS = 0;

    public static <RequestBody extends BaseWebhookRequestBody> WeComWebhookExecutor<RequestBody> newInstance() {
        return new WeComWebhookExecutor();
    }

    @Override
    protected boolean checkResponseObjectHandler(ExecuteResponse<WeComResponse> executeResponse) {
        WeComResponse weComResponse = executeResponse.parsedResponseObject;
        return Objects.equals(ERR_CODE_SUCCESS, weComResponse.getErrcode());
    }

    @Override
    protected WebhookProductEnum webhookProductEnum() {
        return WebhookProductEnum.WE_COM;
    }
}
