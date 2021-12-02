package cn.woodwhales.common.webhook.event;

import cn.woodwhales.common.webhook.enums.WebhookProductEnum;
import cn.woodwhales.common.webhook.plugin.WebhookExtraInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static cn.woodwhales.common.webhook.enums.WebhookProductEnum.getWebhookProductEnumByNoticeUrl;
import static cn.woodwhales.common.webhook.executor.WebhookExecutorFactory.execute;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

/**
 * @author woodwhales on 2021-09-15 16:40
 */
@Slf4j
public class WebhookEventHandler {

    /**
     * 处理通知
     * @param webhookEvent 要发布的通知事件
     * @param noticeUrl 优先使用 WebhookEvent 中的 noticeUrl
     * @param basePackageName 优先使用 WebhookEvent 中的基础包全类名
     * @param webhookExtraInfo 扩展信息对象
     */
    public static void handleCustomEvent(WebhookEvent webhookEvent,
                                         String noticeUrl,
                                         String basePackageName,
                                         WebhookExtraInfo webhookExtraInfo) {
        final String finalNoticeUrl = defaultIfBlank(webhookEvent.getNoticeUrl(), noticeUrl);

        // 从 finalNoticeUrl 中动态获取 WebhookProductEnum
        if(webhookEvent.needFillField()) {
            WebhookProductEnum webhookProductEnum = getWebhookProductEnumByNoticeUrl(finalNoticeUrl);
            webhookEvent.fillField(webhookProductEnum);
        }

        final String finalBasePackageName = defaultIfBlank(webhookEvent.getBasePackageName(), basePackageName);
        webhookEvent.setBasePackName(finalBasePackageName);

        // 增加额外扩展信息
        if(Objects.nonNull(webhookExtraInfo)) {
            webhookEvent.setGitProperties(webhookExtraInfo.getGitProperties());
            webhookEvent.setMachineInfoMap(webhookExtraInfo.getMachineInfoMap());
        }

        log.info("监听到异常报警事件，消息为：{}, 发布时间：{}", webhookEvent.getOccurTime(), webhookEvent.getTitle());
        execute(finalNoticeUrl, webhookEvent.getBaseWebhookRequestBody());
    }

}
