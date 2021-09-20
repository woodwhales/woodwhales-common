package cn.woodwhales.common.webhook.executor;

import cn.woodwhales.common.webhook.enums.WebhookProductEnum;
import cn.woodwhales.common.webhook.model.request.BaseWebhookRequestBody;
import cn.woodwhales.common.webhook.model.request.DingTalkRequestBody;
import cn.woodwhales.common.webhook.model.response.DingTalkResponse;
import cn.woodwhales.common.webhook.model.response.WebhookExecuteResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 钉钉webhook请求执行器
 * @author woodwhales on 2021-07-19 9:34
 * <p>
 * 正常响应报文：
 * {
 *     "errcode": 0,
 *     "errmsg": "ok"
 * }
 * </p>
 *
 */
@Slf4j
public class DingTalkWebhookExecutor<RequestBody extends BaseWebhookRequestBody> extends BaseWebhookExecutor<DingTalkRequestBody, DingTalkResponse> {

    private static final int ERR_CODE_SUCCESS = 0;

    @Override
    protected WebhookProductEnum webhookProductEnum() {
        return WebhookProductEnum.DING_TALK;
    }

    @Override
    protected boolean checkResponseObjectHandler(WebhookExecuteResponse<DingTalkResponse> executeResponse) {
        DingTalkResponse dingTalkResponse = executeResponse.parsedResponseObject;
        return Objects.equals(ERR_CODE_SUCCESS, dingTalkResponse.getErrcode());
    }

    public static <RequestBody extends BaseWebhookRequestBody> DingTalkWebhookExecutor<RequestBody> newInstance() {
        return new DingTalkWebhookExecutor();
    }

}
