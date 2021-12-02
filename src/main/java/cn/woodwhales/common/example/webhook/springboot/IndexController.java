package cn.woodwhales.common.example.webhook.springboot;

import cn.woodwhales.common.webhook.event.WebhookEvent;
import cn.woodwhales.common.webhook.event.WebhookEventFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * springboot 项目使用 webhook 示例
 * @author woodwhales on 2021-07-16 20:46
 */
@RestController
@RequestMapping("test")
public class IndexController {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private RuntimeException exception = new RuntimeException("报错啦");

    /**
     * 发送 webhook 通知
     * @param content 要发送的内容
     * @return 响应对象
     */
    @GetMapping("/send")
    public String send(@RequestParam("content") String content) {

        // 方式1 不推荐，显示创建指定webhook事件对象
        example1(content);

        // 方式1 推荐，不用显示创建指定webhook事件对象，根据通知发送链接自动识别创建对应的webhook事件对象
        example2(content);

        // 方式3 推荐，不带用户id信息，
        example3(content);

        return "ok";
    }

    private void example1(String content) {
        WebhookEvent webhookEvent = WebhookEventFactory.dingTalk(this, "测试标题", exception, request -> {
            request.addContent("content：", content);
            request.addContent("key：", content);
        });
        applicationEventPublisher.publishEvent(webhookEvent);
    }

    private void example2(String content) {
        WebhookEvent webhookEvent = WebhookEventFactory.newWebhookEventWithUserId(this, "测试标题", exception, request -> {
            request.addContent("content：", content);
            request.addContent("key：", content);
        }, Arrays.asList("xxx"));
        applicationEventPublisher.publishEvent(webhookEvent);
    }

    private void example3(String content) {
        final WebhookEvent webhookEvent = WebhookEventFactory.newWebhookEvent(this, "aaa", exception, request -> {
            request.addContent("content：", content);
            request.addContent("key：", content);
        });

        // 发送到指定webhook，不使用默认配置的webhook
        webhookEvent.setNoticeUrl("https://oapi.dingtalk.com/robot/send?access_token=yyy");

        applicationEventPublisher.publishEvent(webhookEvent);
    }

}
