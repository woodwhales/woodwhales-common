package cn.woodwhales.common.webhook.executor;

import cn.woodwhales.common.webhook.enums.WebhookProductEnum;
import cn.woodwhales.common.webhook.model.param.WebhookExecuteParam;
import cn.woodwhales.common.webhook.model.request.BaseWebhookRequestBody;
import cn.woodwhales.common.webhook.model.response.WebhookExecuteResponse;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.ParameterizedType;

import static java.util.Objects.nonNull;

/**
 * 通用 webhook 请求执行器
 * @author woodwhales on 2021-07-16 21:24
 */
@Slf4j
public abstract class BaseWebhookExecutor<RequestBody extends BaseWebhookRequestBody, Response> {

    /**
     * 请求之前的处理方法
     * @param webhookExecuteParam webhook 请求参数
     */
    protected void beforeHandler(WebhookExecuteParam webhookExecuteParam) {
        return;
    }

    /**
     * 请求之后的处理方法
     * @param webhookExecuteParam webhook 请求参数
     * @param executeResponse 响应结果
     */
    protected void afterHandler(WebhookExecuteParam webhookExecuteParam, WebhookExecuteResponse<Response> executeResponse) {
        return;
    }

    /**
     * 校验解析后的响应结果
     * @param executeResponse 响应结果
     * @return 校验是否成功
     */
    protected abstract boolean checkResponseObjectHandler(WebhookExecuteResponse<Response> executeResponse);

    /**
     * 解析响应结果
     * @param executeResponse 响应结果
     * @return 响应结果
     */
    protected Response parseResponseHandler(WebhookExecuteResponse<Response> executeResponse) {
        ParameterizedType genericSuperclass = (ParameterizedType)this.getClass()
                                                                          .getGenericSuperclass();
        Class<Response> clazz = (Class<Response>)genericSuperclass.getActualTypeArguments()[1];
        return new Gson().fromJson(executeResponse.originResponseContent, clazz);
    }

    /**
     * 校验响应结果失败之后的处理
     * @param webhookExecuteParam webhook 请求参数
     * @param executeResponse 响应结果
     */
    protected void checkFailHandler(WebhookExecuteParam webhookExecuteParam, WebhookExecuteResponse<Response> executeResponse) {
        log.error("{}发送消息失败, requestContent = {}, originResponseContent = {}",
                  webhookProductEnum().chineseName,
                  webhookExecuteParam.content,
                  executeResponse.originResponseContent);
    }

    /**
     * 校验响应结果成功之后的处理
     * @param webhookExecuteParam webhook 请求参数
     * @param executeResponse 响应结果
     */
    protected void checkSuccessHandler(WebhookExecuteParam webhookExecuteParam, WebhookExecuteResponse<Response> executeResponse) {
        log.info("{}发送消息成功, requestContent = {}, originResponseContent = {}",
                 webhookProductEnum().chineseName,
                 webhookExecuteParam.content,
                 executeResponse.originResponseContent);
    }

    /**
     * webhook 产品信息
     * @return WebhookProductEnum
     */
    protected abstract WebhookProductEnum webhookProductEnum();

    /**
     * 执行发送消息
     * @param url webhook 通知地址
     * @param content 通知内容
     */
    protected void execute(String url, String content) {
        this.execute(WebhookExecuteParam.newInstance(url, content));
    }

    /**
     * 执行发送消息
     * @param webhookExecuteParam webhook 请求参数
     */
    protected void execute(WebhookExecuteParam webhookExecuteParam) {
        // 请求之前处理
        this.beforeHandler(webhookExecuteParam);

        // 执行请求
        WebhookExecuteResponse<Response> executeResponse = executeRequest(webhookExecuteParam);

        // 解析请求
        executeResponse.parsedResponseObject = this.parseResponseHandler(executeResponse);

        // 校验请求
        executeResponse.checkResult = this.checkResponseObjectHandler(executeResponse);
        if(!executeResponse.checkResult) {
            this.checkFailHandler(webhookExecuteParam, executeResponse);
        } else {
            this.checkSuccessHandler(webhookExecuteParam, executeResponse);
        }

        // 请求之后处理
        this.afterHandler(webhookExecuteParam, executeResponse);
    }

    private WebhookExecuteResponse<Response> executeRequest(WebhookExecuteParam webhookExecuteParam) {
        WebhookExecuteResponse<Response> executeResponse = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()){
            HttpPost post = new HttpPost(webhookExecuteParam.url);
            post.setHeader("Accept","aplication/json");
            post.setHeader("Content-Type", "application/json;charset=UTF-8");
            StringEntity se = new StringEntity(webhookExecuteParam.content, "UTF-8");
            se.setContentEncoding("UTF-8");
            se.setContentType("application/json");
            post.setEntity(se);
            CloseableHttpResponse response = httpClient.execute(post);

            int statusCode = response.getStatusLine().getStatusCode();
            String originResponseContent = EntityUtils.toString(response.getEntity());
            executeResponse  = new WebhookExecuteResponse<>(statusCode, originResponseContent);
        } catch (Exception e) {
            log.error("{}发送消息失败, 异常原因：", e.getMessage(), e);
        }
        return executeResponse;
    }

    public void execute(String url, RequestBody requestBody) {
        if(nonNull(requestBody)) {
            this.execute(url, requestBody.toJsonSting());
        } else {
            log.warn("dingTalkRequestBody is NULL");
        }
    }

    protected Response getParsedResponse(WebhookExecuteResponse<Response> executeResponse) {
        return executeResponse.parsedResponseObject;
    }

}
