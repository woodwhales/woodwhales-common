package cn.woodwhales.common.example.webhook.nonspringboot;

import cn.woodwhales.common.webhook.executor.WebhookExecutorFactory;
import cn.woodwhales.common.webhook.plugin.WebhookExtraInfo;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 非 springboot 项目使用 webhook 示例
 *
 * @author woodwhales on 2021-09-20 21:05
 */
public class WebhookExecutorTest {

    private static WebhookExtraInfo webhookExtraInfo = new WebhookExtraInfo(5, TimeUnit.MINUTES);

    public static void main(String[] args) {
        DingTalkExecutor();
        FeiShuExecutor();
        WeComExecutor();
    }

    public static void DingTalkExecutor() {
        String url = "https://oapi.dingtalk.com/robot/send?access_token=xxx";
        String secret = "yyy";
        String title = "dingding test title";

        WebhookExecutorFactory.Builder.build(url, title, req -> {
                    req.addContent("key1：", "value1");
                    req.addContent("key2：", "value2");
                    req.addContent("key3：", "value3");
                }).secret(secret)
                .throwable(new NullPointerException("报错啦"), "cn.woodwhales", "cn.cloudcoders")
                .webhookExtraInfo(webhookExtraInfo)
                .userMobileList(Arrays.asList("13521349377"))
                .execute();
    }

    public static void FeiShuExecutor() {
        String url = "https://open.feishu.cn/open-apis/bot/v2/hook/xxx";
        String secret = "yyy";
        String title = "feishu test title";

        WebhookExecutorFactory.Builder.build(url, title, req -> {
                    req.addContent("key1：", "value1");
                    req.addContent("key2：", "value2");
                    req.addContent("key3：", "value3");
                }).secret(secret)
                .throwable(new NullPointerException("报错啦"), "cn.woodwhales", "cn.cloudcoders")
                .webhookExtraInfo(webhookExtraInfo)
                .execute();

    }

    public static void WeComExecutor() {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=xxx";
        String title = "wecom test title";
        WebhookExecutorFactory.Builder.build(url, title, req -> {
                    req.addContent("key1：", "value1");
                    req.addContent("key2：", "value2");
                    req.addContent("key3：", "value3");
                })
                .throwable(new NullPointerException("报错啦"), "cn.woodwhales", "cn.cloudcoders")
                .webhookExtraInfo(webhookExtraInfo)
                .execute();
    }

}
