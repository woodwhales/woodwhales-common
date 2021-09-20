package cn.woodwhales.common.webhook.model.request;

import lombok.Data;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 飞书 webhook 请求对象
 * @author woodwhales on 2021-07-19 14:48
 */
@Data
public class FeiShuRequestBody extends BaseWebhookRequestBody {

    public static FeiShuRequestBody newInstance(String title) {
        LinkedList<List<ContentItemDTO>> content = new LinkedList<>();
        PostContentDTO postContentDTO = new PostContentDTO(title, content);
        LanguageContentDTO languageContentDTO = new LanguageContentDTO(postContentDTO);
        ContentDTO contentDTO = new ContentDTO(languageContentDTO);
        FeiShuRequestBody feiShuNoticeRequestBody = new FeiShuRequestBody(contentDTO);
        return feiShuNoticeRequestBody;
    }

    @Override
    public void preToJsonSting() {
        map.entrySet()
           .stream()
           .forEach(entry ->
                this.getContent()
                    .getPost()
                    .getZh_cn()
                    .getContent()
                    .add(Arrays.asList(new ContentItemDTO(entry.getKey()), new ContentItemDTO(entry.getValue())))
           );
    }

    private String msg_type = "post";
    private ContentDTO content;

    public FeiShuRequestBody(ContentDTO content) {
        this.content = content;
    }

    private FeiShuRequestBody() {}

    @Data
    private static class ContentDTO {
        private LanguageContentDTO post;

        public ContentDTO(LanguageContentDTO post) {
            this.post = post;
        }
    }

    @Data
    private static class LanguageContentDTO {
        private PostContentDTO zh_cn;

        public LanguageContentDTO(PostContentDTO zh_cn) {
            this.zh_cn = zh_cn;
        }
    }

    @Data
    private static class PostContentDTO {
        private String title;
        private LinkedList<List<ContentItemDTO>> content;

        public PostContentDTO(String title, LinkedList<List<ContentItemDTO>> content) {
            this.title = title;
            this.content = content;
        }
    }

    @Data
    public static class ContentItemDTO {
        private String tag = "text";
        private String text;

        public ContentItemDTO(String text) {
            this.text = text;
        }
    }

}
