package cn.woodwhales.common.webhook.model.request;

import cn.woodwhales.common.webhook.model.param.ExecuteParam;
import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

/**
 * @author woodwhales on 2021-07-19 14:52
 */
@Data
@Slf4j
public class DingTalkRequestBody extends BaseWebhookRequestBody {

    @Expose
    private String msgtype = "markdown";
    @Expose
    private MarkdownContent markdown = new MarkdownContent();
    @Expose
    private AtDTO at;

    public static DingTalkRequestBody newInstance(String title) {
        DingTalkRequestBody dingTalkRequestBody = new DingTalkRequestBody();

        dingTalkRequestBody.getMarkdown().setTitle(title);
        dingTalkRequestBody.getMap().put("# ", title);
        return dingTalkRequestBody;
    }

    @Override
    public String getUrlAndSignContent(ExecuteParam executeParam) {
        final long timestamp = System.currentTimeMillis();
        final String sign = this.generateSign(executeParam.getSecret(), timestamp, executeParam.getRequestBody());
        return String.format("%s&timestamp=%s&sign=%s", executeParam.getUrl(), timestamp, sign);
    }

    private String generateSign(String secret, long timestamp, BaseWebhookRequestBody requestBody) {
        String stringToSign = timestamp + "\n" + secret;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signData);
        } catch (Exception e) {
            log.error("签名失败");
        }
        return "";
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
        if(CollectionUtils.isNotEmpty(this.userIdList) || CollectionUtils.isNotEmpty(this.userMobileList)) {
            this.at = new AtDTO(this.userIdList, this.userMobileList);
        }
    }

    @Data
    private static class MarkdownContent {
        @Expose
        private String title;
        @Expose
        private String text;
    }

    @Data
    private static class AtDTO {
        @Expose
        private List<String> atMobiles;
        @Expose
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
