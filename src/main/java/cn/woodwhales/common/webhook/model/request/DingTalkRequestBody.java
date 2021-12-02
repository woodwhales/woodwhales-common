package cn.woodwhales.common.webhook.model.request;

import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @author woodwhales on 2021-07-19 14:52
 */
@Data
public class DingTalkRequestBody extends BaseWebhookRequestBody {

    private String msgtype = "markdown";
    private MarkdownContent markdown = new MarkdownContent();
    private AtDTO at;

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
        this.at = new AtDTO(this.userIdList, this.userMobileList);
    }

    @Data
    private static class MarkdownContent {
        private String title;
        private String text;
    }

    @Data
    private static class AtDTO {
        private List<String> atMobiles;
        private List<String> atUserIds;

        public AtDTO(List<String> userMobileList, List<String> userIdList) {
            if(userMobileList != null && userMobileList.size() > 0) {
                this.atMobiles = userMobileList;
            }
            if(userIdList != null && userIdList.size() > 0) {
                this.atUserIds = userIdList;
            }
        }
    }

}
