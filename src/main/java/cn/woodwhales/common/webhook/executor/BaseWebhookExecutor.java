package cn.woodwhales.common.webhook.executor;

import cn.woodwhales.common.webhook.enums.WebhookProductEnum;
import cn.woodwhales.common.webhook.model.param.ExecuteParam;
import cn.woodwhales.common.webhook.model.request.BaseWebhookRequestBody;
import cn.woodwhales.common.webhook.model.response.ExecuteResponse;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.nonNull;

/**
 * 请求执行器
 * @author woodwhales on 2021-07-16 21:24
 */
@Slf4j
public abstract class BaseWebhookExecutor<RequestBody extends BaseWebhookRequestBody, Response> {

    /**
     * 请求之前的处理方法
     * @param executeParam executeParam
     */
    protected void beforeHandler(ExecuteParam executeParam) {
        return;
    }

    /**
     * 请求之后的处理方法
     * @param executeParam executeParam
     * @param executeResponse executeResponse
     */
    protected void afterHandler(ExecuteParam executeParam, ExecuteResponse<Response> executeResponse) {
        return;
    }

    /**
     * 校验解析后的响应结果
     * @param executeResponse  executeResponse
     * @return 是都校验通过
     */
    protected abstract boolean checkResponseObjectHandler(ExecuteResponse<Response> executeResponse);

    /**
     * 解析响应结果
     * @param executeResponse executeResponse
     * @return 解析响应结果
     */
    protected Response parseResponseHandler(ExecuteResponse<Response> executeResponse) {
        ParameterizedType genericSuperclass = (ParameterizedType)this.getClass()
                .getGenericSuperclass();
        Class<Response> clazz = (Class<Response>)genericSuperclass.getActualTypeArguments()[1];
        return new Gson().fromJson(executeResponse.originResponseContent, clazz);
    }

    /**
     * 校验响应结果失败之后的处理
     * @param executeResponse executeResponse
     */
    protected void checkFailHandler(ExecuteResponse<Response> executeResponse) {
        log.error("{}发送消息失败, url = {}, requestContent = {}, originResponseContent = {}", webhookProductEnum().chineseName,
                                                                                            executeResponse.url,
                                                                                            executeResponse.requestContent,
                                                                                            executeResponse.originResponseContent);
    }

    /**
     * 校验响应结果成功之后的处理
     * @param executeResponse executeResponse
     */
    protected void checkSuccessHandler(ExecuteResponse<Response> executeResponse) {
        log.info("{}发送消息成功, url = {}, requestContent = {}, originResponseContent = {}", webhookProductEnum().chineseName,
                                                                                            executeResponse.url,
                                                                                            executeResponse.requestContent,
                                                                                            executeResponse.originResponseContent);
    }

    /**
     * webhook 产品信息
     * @return WebhookProductEnum
     */
    protected abstract WebhookProductEnum webhookProductEnum();

    /**
     * 执行发送消息
     * @param executeParam executeParam
     */
    protected void execute(ExecuteParam executeParam) {
        // 请求之前处理
        this.beforeHandler(executeParam);

        // 执行请求
        ExecuteResponse<Response> executeResponse = this.executeRequest(executeParam);

        // 解析响应
        executeResponse.parsedResponseObject = this.parseResponseHandler(executeResponse);

        // 校验响应
        executeResponse.checkResult = this.checkResponseObjectHandler(executeResponse);
        if(!executeResponse.checkResult) {
            this.checkFailHandler(executeResponse);
        } else {
            this.checkSuccessHandler(executeResponse);
        }

        // 请求之后处理
        this.afterHandler(executeParam, executeResponse);
    }

    private ExecuteResponse<Response> executeRequest(ExecuteParam executeParam) {
        ExecuteResponse<Response> executeResponse = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final String noticeUrl = executeParam.getNoticeUrl();
            HttpPost post = new HttpPost(noticeUrl);
            post.setHeader("Accept","aplication/json");
            post.setHeader("Content-Type", "application/json;charset=UTF-8");
            final String noticeContent = executeParam.getNoticeContent();
            StringEntity se = new StringEntity(noticeContent, StandardCharsets.UTF_8);
            se.setContentEncoding("UTF-8");
            se.setContentType("application/json");
            post.setEntity(se);
            CloseableHttpResponse response = httpClient.execute(post);

            int statusCode = response.getStatusLine().getStatusCode();
            String originResponseContent = EntityUtils.toString(response.getEntity());
            executeResponse  = new ExecuteResponse<>(noticeUrl, noticeContent, statusCode, originResponseContent);
        } catch (Exception e) {
            log.error("{}发送消息失败, 异常原因：", e.getMessage(), e);
        }
        return executeResponse;
    }

    /**
     * 执行请求
     * @param url 请求地址
     * @param requestBody 请求报文
     */
    public void execute(String url, RequestBody requestBody) {
        execute(url, null, requestBody);
    }

    /**
     * 执行请求
     * @param url 请求地址
     * @param secret 密钥
     * @param requestBody 请求对象
     */
    public void execute(String url, String secret, RequestBody requestBody) {
        if(nonNull(requestBody)) {
            this.execute(ExecuteParam.newInstance(url, secret, requestBody));
        } else {
            log.warn("dingTalkRequestBody is NULL");
        }
    }

    protected Response getParsedResponse(ExecuteResponse<Response> executeResponse) {
        return executeResponse.parsedResponseObject;
    }

}
