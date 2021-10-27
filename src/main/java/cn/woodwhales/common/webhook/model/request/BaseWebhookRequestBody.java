package cn.woodwhales.common.webhook.model.request;

import cn.woodwhales.common.util.JsonUtils;
import cn.woodwhales.common.webhook.enums.WebhookProductEnum;
import cn.woodwhales.common.webhook.model.WebhookGlobalInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用 webhook 请求对象
 * @author woodwhales on 2021-07-19 14:52
 */
public abstract class BaseWebhookRequestBody {

    @JsonIgnore
    protected WebhookProductEnum webhookProductEnum;

    @JsonIgnore
    protected WebhookGlobalInfo webhookGlobalInfo;

    @JsonIgnore
    protected List<String> userIdList;

    @JsonIgnore
    protected List<String> userMobileList;

    @JsonIgnore
    protected Map<String, String> map = new LinkedHashMap<>();

    public String toJsonSting() {
        List<Pair<String, String>> allInfoPair = this.webhookGlobalInfo.getAllInfoPair();
        allInfoPair.stream().forEach(pair -> map.put(pair.getLeft(), pair.getRight()));
        preToJsonSting();
        return JsonUtils.toJson(this);
    }

    /**
     * 添加用户id集合
     * @param userIdList 用户id集合
     * @return BaseWebhookRequestBody 对象
     */
    protected BaseWebhookRequestBody addUserIdList(List<String> userIdList) {
        if(userIdList != null && userIdList.size() > 0) {
            this.userIdList = userIdList;
        }
        return this;
    }

    /**
     * 添加用户手机号集合
     * @param userMobileList 用户手机号集合
     * @return BaseWebhookRequestBody 对象
     */
    protected BaseWebhookRequestBody addUserMobileList(List<String> userMobileList) {
        if(userMobileList != null && userMobileList.size() > 0) {
            this.userMobileList = userMobileList;
        }
        return this;
    }

    /**
     * 对象转json字符串之前的操作
     */
    public abstract void preToJsonSting();

    public Map<String, String> getMap() {
        return map;
    }

    /**
     * 添加 k-v 信息
     * @param tag key
     * @param text value
     * @return BaseWebhookRequestBody
     */
    public BaseWebhookRequestBody addContent(String tag, String text) {
        this.map.put(tag, text);
        return this;
    }

    public void setWebhookProductEnum(WebhookProductEnum webhookProductEnum) {
        this.webhookProductEnum = webhookProductEnum;
    }

    public WebhookProductEnum getWebhookProductEnum() {
        return webhookProductEnum;
    }

    public BaseWebhookRequestBody addGlobalInfo(WebhookGlobalInfo webhookGlobalInfo) {
        this.webhookGlobalInfo = webhookGlobalInfo;
        return this;
    }

}
