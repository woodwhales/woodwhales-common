package cn.woodwhales.common.example.webhook.springboot;

import cn.woodwhales.common.webhook.event.WebhookEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * springboot 项目使用 webhook 示例
 *
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
     *
     * @param content 要发送的内容
     * @return 响应对象
     */
    @GetMapping("/send")
    public String send(@RequestParam("content") String content) {

        example1(content);
        example2(content);
        example3(content);

        return "ok";
    }

    private void example1(String content) {
        applicationEventPublisher.publishEvent(WebhookEvent.Builder.build(this, "测试标题")
                                                                    .throwable(exception, "cn.woodwhales", "cn.cloudcoders")
                                                                    .consumer(request -> {
                                                                        request.addContent("content：", content);
                                                                        request.addContent("key：", content);
                                                                    })
                                                                    .build());
    }

    private void example2(String content) {
        applicationEventPublisher.publishEvent(WebhookEvent.Builder.build(this, "测试标题")
                                                                    .throwable(exception, "cn.woodwhales", "cn.cloudcoders")
                                                                    .consumer(request -> {
                                                                        request.addContent("content：", content);
                                                                        request.addContent("key：", content);
                                                                    })
                                                                    .userIdList(Arrays.asList("xxx")).build());
    }

    private void example3(String content) {
        applicationEventPublisher.publishEvent(WebhookEvent.Builder.build(this, "测试标题")
                                                .throwable(exception, "cn.woodwhales", "cn.cloudcoders")
                                                .consumer(request -> {
                                                    request.addContent("content：", content);
                                                    request.addContent("key：", content);
                                                })
                                                // 发送到指定webhook，不使用默认配置的webhook
                                                .noticeUrl("https://oapi.dingtalk.com/robot/send?access_token=yyy").build()
        );
    }

}
