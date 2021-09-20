package cn.woodwhales.common.webhook.model.request;

import lombok.Data;

import java.util.Objects;

/**
 * 企业微信 webhook 请求对象
 * @author woodwhales on 2021-07-19 18:26
 */
@Data
public class WeComRequestBody extends BaseWebhookRequestBody {

    private String msgtype = "markdown";

    private MarkdownContent markdown = new MarkdownContent();

    public static WeComRequestBody newInstance(String title) {
        WeComRequestBody feiShuNoticeRequestBody = new WeComRequestBody();
        feiShuNoticeRequestBody.getMap().put("# ", title);
        return feiShuNoticeRequestBody;
    }

    @Override
    public void preToJsonSting() {
        StringBuilder stringBuilder = new StringBuilder();
        if(Objects.nonNull(map) && !map.isEmpty()) {
            map.entrySet().stream().forEach(entry ->
                                                stringBuilder.append(entry.getKey())
                                                             .append(entry.getValue())
                                                             .append(" \n")
            );
        }
        this.markdown.setContent(stringBuilder.toString());
    }

    @Data
    private static class MarkdownContent {
        private String content;
    }

}
