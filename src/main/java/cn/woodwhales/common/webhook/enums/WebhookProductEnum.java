package cn.woodwhales.common.webhook.enums;

import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * webhook 类型枚举
 *
 * @author woodwhales on 2021-07-16 21:09
 */
@Slf4j
public enum WebhookProductEnum {

    /**
     * 企业微信
     */
    WE_COM("WE_COM", "企业微信", 4096 / 3, "qyapi.weixin.qq.com"),

    /**
     * 钉钉
     */
    DING_TALK("DING_TALK", "钉钉", 4096 / 3, "oapi.dingtalk.com"),

    /**
     * 飞书
     */
    FEI_SHU("FEI_SHU", "飞书", 20_000 / 3, "open.feishu.cn"),
    ;

    public final String code;
    public final int limitContentLength;
    public final String chineseName;
    public final String host;

    WebhookProductEnum(String code, String chineseName, int limitContentLength, String host) {
        this.code = code;
        this.limitContentLength = limitContentLength;
        this.chineseName = chineseName;
        this.host = host;
    }

    private static final Map<String, WebhookProductEnum> hostMap;

    static {
        hostMap = new HashMap<>();
        WebhookProductEnum[] webhookProductEnums = WebhookProductEnum.values();
        for (WebhookProductEnum webhookProductEnum : webhookProductEnums) {
            hostMap.put(webhookProductEnum.host, webhookProductEnum);
        }
    }

    public static WebhookProductEnum getWebhookProductEnumByNoticeUrl(String noticeUrl) {
        URL url;
        try {
            url = new URL(noticeUrl);
            String host = url.getHost();
            return hostMap.get(host);
        } catch (MalformedURLException e) {
            log.error("noticeUrl=[{}], 配置非法", noticeUrl);
        }
        return null;
    }

}
