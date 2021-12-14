package cn.woodwhales.common.webhook.model.request;

import cn.woodwhales.common.webhook.enums.WebhookProductEnum;
import cn.woodwhales.common.webhook.model.GlobalInfo;
import cn.woodwhales.common.webhook.model.param.ExecuteParam;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author woodwhales on 2021-07-19 14:52
 */
public abstract class BaseWebhookRequestBody {

    @JsonIgnore
    @Expose(serialize = false)
    protected WebhookProductEnum webhookProductEnum;

    @JsonIgnore
    @Expose(serialize = false)
    protected GlobalInfo globalInfo;

    @JsonIgnore
    @Expose(serialize = false)
    protected List<String> userIdList;

    @JsonIgnore
    @Expose(serialize = false)
    protected List<String> userMobileList;

    @JsonIgnore
    @Expose(serialize = false)
    protected Map<String, Object> map = new LinkedHashMap<>();

    public String toJsonSting() {
        List<Pair<String, String>> allInfoPair = this.globalInfo.getAllInfoPair();
        allInfoPair.stream().forEach(pair -> map.put(pair.getLeft(), pair.getRight()));
        preToJsonSting();
        final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }

    /**
     * 对内容签名并返回 url
     * @param executeParam 请求对象
     * @return 签名的通知地址
     */
    public abstract String getUrlAndSignContent(ExecuteParam executeParam);

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

    public Map<String, Object> getMap() {
        return map;
    }

    public BaseWebhookRequestBody addContent(String tag, String text) {
        this.map.put(tag, text);
        return this;
    }

    public BaseWebhookRequestBody addContent(String tag, Object obj) {
        this.map.put(tag, obj);
        return this;
    }

    public BaseWebhookRequestBody addSign(long timestamp, String sign) {
        return this.addContent("sign", sign).addContent("timestamp", timestamp);
    }

    public void setWebhookProductEnum(WebhookProductEnum webhookProductEnum) {
        this.webhookProductEnum = webhookProductEnum;
    }

    public WebhookProductEnum getWebhookProductEnum() {
        return webhookProductEnum;
    }

    public BaseWebhookRequestBody addGlobalInfo(GlobalInfo globalInfo) {
        this.globalInfo = globalInfo;
        return this;
    }

}