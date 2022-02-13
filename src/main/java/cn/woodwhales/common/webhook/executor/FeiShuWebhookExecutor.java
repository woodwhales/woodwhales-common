package cn.woodwhales.common.webhook.executor;

import cn.woodwhales.common.webhook.enums.WebhookProductEnum;
import cn.woodwhales.common.webhook.model.request.BaseWebhookRequestBody;
import cn.woodwhales.common.webhook.model.request.FeiShuRequestBody;
import cn.woodwhales.common.webhook.model.response.FeiShuResponse;
import cn.woodwhales.common.webhook.model.response.ExecuteResponse;

import java.util.Objects;

/**
 * 飞书webhook请求执行器
 *
 * @author woodwhales on 2021-07-19 10:36
 * <p>
 * {
 * "code": 19024,
 * "msg": "Key Words Not Found"
 * }
 */
public class FeiShuWebhookExecutor<RequestBody extends BaseWebhookRequestBody> extends BaseWebhookExecutor<FeiShuRequestBody, FeiShuResponse> {

    private static final int ERR_CODE_SUCCESS = 0;

    public static <RequestBody extends BaseWebhookRequestBody> FeiShuWebhookExecutor<RequestBody> newInstance() {
        return new FeiShuWebhookExecutor();
    }

    @Override
    protected boolean checkResponseObjectHandler(ExecuteResponse<FeiShuResponse> executeResponse) {
        FeiShuResponse feiShuResponse = executeResponse.parsedResponseObject;
        return Objects.equals(ERR_CODE_SUCCESS, feiShuResponse.getStatusCode());
    }

    @Override
    protected WebhookProductEnum webhookProductEnum() {
        return WebhookProductEnum.FEI_SHU;
    }
}
