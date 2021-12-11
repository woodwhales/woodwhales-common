package cn.woodwhales.common.webhook.model.request;

import cn.woodwhales.common.webhook.model.param.ExecuteParam;
import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author woodwhales on 2021-07-19 14:48
 */
@Data
@Slf4j
public class FeiShuRequestBody extends BaseWebhookRequestBody {

    @Expose
    private String msg_type = "post";

    @Expose
    private ContentDTO content;

    @Expose
    protected String sign;

    @Expose
    protected String timestamp;

    public static FeiShuRequestBody newInstance(String title) {
        LinkedList<List<BaseContentItemDTO>> content = new LinkedList<>();
        PostContentDTO postContentDTO = new PostContentDTO(title, content);
        LanguageContentDTO languageContentDTO = new LanguageContentDTO(postContentDTO);
        ContentDTO contentDTO = new ContentDTO(languageContentDTO);
        FeiShuRequestBody feiShuNoticeRequestBody = new FeiShuRequestBody(contentDTO);
        return feiShuNoticeRequestBody;
    }

    @Override
    public String getUrlAndSignContent(ExecuteParam executeParam) {
        final Long timestamp = Long.parseLong(StringUtils.substring(System.currentTimeMillis() + "", 0 ,10));
        final String sign = this.generateSign(executeParam.getSecret(), timestamp, executeParam.getRequestBody());
        this.timestamp = timestamp.toString();
        this.sign = sign;
        return executeParam.getUrl();
    }

    @Override
    public void preToJsonSting() {
        LinkedList<List<BaseContentItemDTO>> contentList = this.getContent()
                .getPost()
                .getZh_cn()
                .getContent();
        map.entrySet()
                .stream()
                .forEach(entry -> {
                    List<BaseContentItemDTO> list = new ArrayList<>();
                    list.add(new ContentItemDTO(entry.getKey()));
                    list.add(new ContentItemDTO((String)entry.getValue()));
                    contentList.add(list);
                });

        List<BaseContentItemDTO> baseContentItemDTOS = contentList.get(0);
        if(this.userIdList != null && this.userIdList.size() > 0) {
            baseContentItemDTOS.addAll(this.userIdList.stream().map(ContentUserItemDTO::new).collect(Collectors.toList()));
        }
    }

    private String generateSign(String secret, long timestamp, BaseWebhookRequestBody requestBody) {
        String stringToSign = timestamp + "\n" + secret;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal();
            return Base64.getEncoder().encodeToString(signData);
        } catch (Exception e) {
            log.error("签名失败");
        }
        return "";
    }

    public FeiShuRequestBody(ContentDTO content) {
        this.content = content;
    }

    private FeiShuRequestBody() {}

    @Data
    private static class ContentDTO {
        @Expose
        private LanguageContentDTO post;

        public ContentDTO(LanguageContentDTO post) {
            this.post = post;
        }
    }

    @Data
    private static class LanguageContentDTO {
        @Expose
        private PostContentDTO zh_cn;

        public LanguageContentDTO(PostContentDTO zh_cn) {
            this.zh_cn = zh_cn;
        }
    }

    @Data
    private static class PostContentDTO {
        @Expose
        private String title;
        @Expose
        private LinkedList<List<BaseContentItemDTO>> content;

        public PostContentDTO(String title, LinkedList<List<BaseContentItemDTO>> content) {
            this.title = title;
            this.content = content;
        }
    }

    @Data
    public static class ContentItemDTO extends BaseContentItemDTO {
        @Expose
        private String tag = "text";
        @Expose
        private String text;

        public ContentItemDTO(String text) {
            this.text = text;
        }
    }

    public static class BaseContentItemDTO {
    }

    @Data
    public static class ContentUserItemDTO extends BaseContentItemDTO {
        @Expose
        private String tag = "at";
        @Expose
        private String user_id;

        public ContentUserItemDTO(String user_id) {
            this.user_id = user_id;
        }
    }
}
