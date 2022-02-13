package cn.woodwhales.common.webhook.event;

import cn.hutool.core.date.DatePattern;
import cn.woodwhales.common.webhook.executor.WebhookExecutorFactory;
import cn.woodwhales.common.webhook.plugin.WebhookExtraInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.split;

/**
 * @author woodwhales on 2021-09-15 16:40
 */
@Slf4j
public class WebhookEventHandler {

    /**
     * 处理通知
     *
     * @param webhookEvent     要发布的通知事件
     * @param noticeUrl        优先使用 WebhookEvent 中的 noticeUrl
     * @param secret           签名密钥
     * @param basePackageName  优先使用 WebhookEvent 中的基础包全类名
     * @param webhookExtraInfo 扩展信息对象
     */
    public static void handleCustomEvent(WebhookEvent webhookEvent,
                                         String noticeUrl,
                                         String secret,
                                         String basePackageName,
                                         WebhookExtraInfo webhookExtraInfo) {
        final String finalNoticeUrl = defaultIfBlank(webhookEvent.getNoticeUrl(), noticeUrl);
        final String finalSecret = defaultIfBlank(webhookEvent.getSecret(), secret);
        final String[] finalBasePackageNames = Objects.nonNull(webhookEvent.getBasePackageNames()) ? webhookEvent.getBasePackageNames() : split(basePackageName, ",");

        log.info("监听到异常报警事件，消息标题：{}, 发布时间：{}", webhookEvent.getTitle(), DateFormatUtils.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN));
        WebhookExecutorFactory.Builder
                .build(finalNoticeUrl, webhookEvent.getTitle(), webhookEvent.getBaseWebhookRequestBody())
                .throwable(webhookEvent.getThrowable(), finalBasePackageNames)
                .userIdList(webhookEvent.getUserIdList())
                .userMobileList(webhookEvent.getUserMobileList())
                .secret(finalSecret)
                .webhookExtraInfo(webhookExtraInfo)
                .execute();
    }

}
