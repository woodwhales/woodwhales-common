package cn.woodwhales.common.example.webhook;

import cn.woodwhales.common.webhook.event.WebhookEvent;
import cn.woodwhales.common.webhook.event.WebhookEventHandler;
import cn.woodwhales.common.webhook.plugin.WebhookExtraInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.concurrent.TimeUnit;

/**
 * webhook使用示例
 * @author woodwhales on 2021-09-15 12:13
 */
@Log4j2
@Configuration
public class ApplicationEventConfig {

    /**
     * 在 application.yml 中配置 notice.url
     */
    @Value("${notice.url}")
    private String noticeUrl;

    /**
     * 项目基包路径
     */
    private String basePackageName = "cn.woodwhales.webhook";

    /**
     * 注入 webhook 插件
     * @return WebhookExtraInfo
     */
    @Bean
    public WebhookExtraInfo webhookExtraInfo() {
        return new WebhookExtraInfo(5, TimeUnit.MINUTES);
    }

    /**
     * 监听 WebhookEvent 事件
     * @param webhookEvent
     */
    @EventListener
    public void handleCustomEvent(WebhookEvent webhookEvent) {
        WebhookEventHandler.handleCustomEvent(webhookEvent, noticeUrl, basePackageName, webhookExtraInfo());
    }

}
