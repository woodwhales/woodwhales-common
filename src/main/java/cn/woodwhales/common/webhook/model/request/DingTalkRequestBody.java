package cn.woodwhales.common.webhook.model.request;

import lombok.Data;

import java.util.Objects;

/**
 * 钉钉 webhook 请求对象
 * @author woodwhales on 2021-07-19 14:52
 */
@Data
public class DingTalkRequestBody extends BaseWebhookRequestBody {

    private String msgtype = "markdown";
    private MarkdownContent markdown = new MarkdownContent();

    public static DingTalkRequestBody newInstance(String title) {
        DingTalkRequestBody dingTalkRequestBody = new DingTalkRequestBody();
        dingTalkRequestBody.getMarkdown().setTitle(title);
        dingTalkRequestBody.getMap().put("# ", title);
        return dingTalkRequestBody;
    }

    @Override
    public void preToJsonSting() {
        StringBuilder stringBuilder = new StringBuilder();
        if(Objects.nonNull(this.map) && !this.map.isEmpty()) {
            this.map.entrySet().stream().forEach(entry ->
                                                stringBuilder.append(entry.getKey())
                                                             .append(entry.getValue())
                                                             .append(" \n\r")
            );
        }
        this.markdown.setText(stringBuilder.toString());
    }

    @Data
    private static class MarkdownContent {
        private String title;
        private String text;
    }

}
