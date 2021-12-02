package cn.woodwhales.common.webhook.executor;

import cn.woodwhales.common.webhook.enums.WebhookProductEnum;
import cn.woodwhales.common.webhook.model.GlobalInfo;
import cn.woodwhales.common.webhook.model.request.BaseWebhookRequestBody;
import cn.woodwhales.common.webhook.model.request.WebhookRequestBodyFactory;
import org.junit.jupiter.api.Test;

class BaseWebhookExecutorTest {
    @Test
    public void DingTalkExecutor() {
        String url = "https://oapi.dingtalk.com/robot/send?access_token=xxx";

        BaseWebhookRequestBody requestBody = WebhookRequestBodyFactory.newInstance(WebhookProductEnum.DING_TALK, "test title");
        requestBody.addContent("key1：", "value1");
        requestBody.addContent("key2：", "value2");
        requestBody.addContent("key3：", "value3");

        GlobalInfo globalInfo = new GlobalInfo(WebhookProductEnum.DING_TALK, new NullPointerException("报错啦"), "cn.woodwhales.webhook");
        requestBody.addGlobalInfo(globalInfo);

        WebhookExecutorFactory.execute(url, requestBody);
    }

    @Test
    public void FeiShuExecutor() {
        String url = "https://open.feishu.cn/open-apis/bot/v2/hook/xxx";

        BaseWebhookRequestBody requestBody = WebhookRequestBodyFactory.newInstance(WebhookProductEnum.FEI_SHU, "test title");
        requestBody.addContent("key1：", "value1");
        requestBody.addContent("key2：", "value2");
        requestBody.addContent("key3：", "value3");

        GlobalInfo globalInfo = new GlobalInfo(WebhookProductEnum.FEI_SHU, new NullPointerException("报错啦"), "cn.woodwhales.webhook");
        requestBody.addGlobalInfo(globalInfo);

        WebhookExecutorFactory.execute(url, requestBody);
    }

    @Test
    public void WeComExecutor() {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=xxx";

        WebhookExecutorFactory.execute(WebhookProductEnum.WE_COM, url, "test title", req -> {
            req.addContent("key1：", "value1");
            req.addContent("key2：", "value2");
            req.addContent("key3：", "value3");
            GlobalInfo globalInfo = new GlobalInfo(WebhookProductEnum.WE_COM, new NullPointerException("报错啦"), "cn.woodwhales.webhook");
            req.addGlobalInfo(globalInfo);
        });
    }
}