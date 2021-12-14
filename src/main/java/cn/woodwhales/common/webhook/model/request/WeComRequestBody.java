package cn.woodwhales.common.webhook.model.request;

import cn.woodwhales.common.webhook.model.param.ExecuteParam;
import com.google.gson.annotations.Expose;
import lombok.Data;

import java.util.Objects;

/**
 * @author woodwhales on 2021-07-19 18:26
 */
@Data
public class WeComRequestBody extends BaseWebhookRequestBody {

    @Expose
    private String msgtype = "markdown";

    @Expose
    private MarkdownContent markdown = new MarkdownContent();

    public static WeComRequestBody newInstance(String title) {
        WeComRequestBody feiShuNoticeRequestBody = new WeComRequestBody();
        feiShuNoticeRequestBody.getMap().put("# ", title);
        return feiShuNoticeRequestBody;
    }

    @Override
    public String getUrlAndSignContent(ExecuteParam executeParam) {
        return executeParam.getUrl();
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
        @Expose
        private String content;
    }

}
