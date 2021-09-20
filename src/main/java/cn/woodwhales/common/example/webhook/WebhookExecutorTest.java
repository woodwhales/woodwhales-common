package cn.woodwhales.common.example.webhook;

import cn.woodwhales.common.webhook.enums.WebhookProductEnum;
import cn.woodwhales.common.webhook.executor.WebhookExecutorFactory;
import cn.woodwhales.common.webhook.model.WebhookGlobalInfo;
import cn.woodwhales.common.webhook.model.request.BaseWebhookRequestBody;
import cn.woodwhales.common.webhook.model.request.WebhookRequestBodyFactory;

/**
 * 非 springboot 项目使用 webhook 示例
 * @author woodwhales on 2021-09-20 21:05
 */
public class WebhookExecutorTest {

    public static void main(String[] args) {
        // 方式一
        DingTalkExecutor();
        FeiShuExecutor();

        // 方式二
        WeComExecutor();
    }

    public static void DingTalkExecutor() {
        String url = "https://oapi.dingtalk.com/robot/send?access_token=xxx";

        BaseWebhookRequestBody requestBody = WebhookRequestBodyFactory.newInstance(WebhookProductEnum.DING_TALK, "test title");
        requestBody.addContent("key1：", "value1");
        requestBody.addContent("key2：", "value2");
        requestBody.addContent("key3：", "value3");

        WebhookGlobalInfo globalInfo = new WebhookGlobalInfo(WebhookProductEnum.DING_TALK, new NullPointerException("报错啦"), "cn.woodwhales.webhook");
        requestBody.addGlobalInfo(globalInfo);

        WebhookExecutorFactory.execute(url, requestBody);
    }

    public static void FeiShuExecutor() {
        String url = "https://open.feishu.cn/open-apis/bot/v2/hook/xxx";

        BaseWebhookRequestBody requestBody = WebhookRequestBodyFactory.newInstance(WebhookProductEnum.FEI_SHU, "test title");
        requestBody.addContent("key1：", "value1");
        requestBody.addContent("key2：", "value2");
        requestBody.addContent("key3：", "value3");

        WebhookGlobalInfo globalInfo = new WebhookGlobalInfo(WebhookProductEnum.FEI_SHU, new NullPointerException("报错啦"), "cn.woodwhales.webhook");
        requestBody.addGlobalInfo(globalInfo);

        WebhookExecutorFactory.execute(url, requestBody);
    }

    public static void WeComExecutor() {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=xxx";

        WebhookExecutorFactory.execute(WebhookProductEnum.WE_COM, url, "test title", req -> {
            req.addContent("key1：", "value1");
            req.addContent("key2：", "value2");
            req.addContent("key3：", "value3");
            WebhookGlobalInfo globalInfo = new WebhookGlobalInfo(WebhookProductEnum.WE_COM, new NullPointerException("报错啦"), "cn.woodwhales.webhook");
            req.addGlobalInfo(globalInfo);
        });
    }

}
