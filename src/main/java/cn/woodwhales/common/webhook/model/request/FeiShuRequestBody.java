package cn.woodwhales.common.webhook.model.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 飞书 webhook 请求对象
 * @author woodwhales on 2021-07-19 14:48
 */
@Data
public class FeiShuRequestBody extends BaseWebhookRequestBody {

    public static FeiShuRequestBody newInstance(String title) {
        LinkedList<List<BaseContentItemDTO>> content = new LinkedList<>();
        PostContentDTO postContentDTO = new PostContentDTO(title, content);
        LanguageContentDTO languageContentDTO = new LanguageContentDTO(postContentDTO);
        ContentDTO contentDTO = new ContentDTO(languageContentDTO);
        FeiShuRequestBody feiShuNoticeRequestBody = new FeiShuRequestBody(contentDTO);
        return feiShuNoticeRequestBody;
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
        private LinkedList<List<BaseContentItemDTO>> content;

        public PostContentDTO(String title, LinkedList<List<BaseContentItemDTO>> content) {
            this.title = title;
            this.content = content;
        }
    }

    @Data
    public static class ContentItemDTO extends BaseContentItemDTO {
        private String tag = "text";
        private String text;

        public ContentItemDTO(String text) {
            this.text = text;
        }
    }

    public static class BaseContentItemDTO {
    }

    @Data
    public static class ContentUserItemDTO extends BaseContentItemDTO {
        private String tag = "at";
        private String user_id;

        public ContentUserItemDTO(String userId) {
            this.user_id = userId;
        }
    }

}
