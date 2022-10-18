package cn.woodwhales.common.util;

import cn.hutool.core.lang.UUID;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.EnumerationUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author woodwhales on 2022-09-21 12:28
 */
@Slf4j
public class RequestAspectTool {

    /**
     * 对请求controller进行切面
     * 使用uuid作为请求流水号，请求之前将请求流水号设置为当前线程名称
     *
     * @param joinPoint joinPoint
     * @return 请求执行结果
     * @throws Throwable 异常对象
     */
    public static Object aspect(ProceedingJoinPoint joinPoint) throws Throwable {
        return aspect(joinPoint,
                () -> UUID.randomUUID().toString(true),
                request -> {
                    request.setAttribute("traceId", Thread.currentThread().getName());
                }, null);
    }

    /**
     * 对请求controller进行切面
     *
     * @param joinPoint         joinPoint
     * @param requestIdSupplier 请求流水号生成接口
     * @param beforeConsumer    请求执行之前的接口
     * @param afterConsumer     请求执行之后的接口
     * @return 请求执行结果
     * @throws Throwable 异常对象
     */
    public static Object aspect(ProceedingJoinPoint joinPoint,
                                Supplier<String> requestIdSupplier,
                                Consumer<HttpServletRequest> beforeConsumer,
                                BiConsumer<HttpServletRequest, Object> afterConsumer) throws Throwable {
        // 将当前线程名称改为 UUID
        if (Objects.nonNull(requestIdSupplier)) {
            String requestId = requestIdSupplier.get();
            if (StringUtils.isNotBlank(requestId)) {
                Thread.currentThread().setName(requestId);
            }
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Signature signature = joinPoint.getSignature();
        String declaringTypeName = signature.getDeclaringType()
                .getName();
        String fullMethodName = declaringTypeName + "#" + signature.getName();
        StringBuilder requestParamBuilder = new StringBuilder();
        String clientIpAddress;
        String requestUrl = null;
        HttpServletRequest request;
        try {
            log.info("======================= {} start =======================", fullMethodName);
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            clientIpAddress = IpTool.getIpAddress(request);
            String methodType = request.getMethod();
            requestUrl = String.format("[ %s ] %s", methodType, request.getRequestURL()
                    .toString());
            // 请求地址
            log.info("request_url={}", requestUrl);
            // 请求ip
            log.info("request_ip={}", clientIpAddress);
            // 请求参数
            log.info("request_param={}", requestParamBuilder.append(getParamStringFromRequest(request)));
            if (!StringUtils.equals(RequestMethod.GET.name(), methodType)) {
                Object requestBodyParam = collectRequestBodyParam(joinPoint.getArgs(), signature);
                if (null != requestBodyParam) {
                    log.info("request_requestBody={}", requestParamBuilder.append(JsonTool.toJSONString(requestBodyParam)));
                }
            }
            //请求头信息
            if (Objects.nonNull(beforeConsumer)) {
                beforeConsumer.accept(request);
            }
            Object result = joinPoint.proceed();
            printResponseBody(result);
            if (Objects.nonNull(afterConsumer)) {
                afterConsumer.accept(request, result);
            }
            return result;
        } catch (Throwable throwable) {
            String requestParam = requestParamBuilder.toString();
            log.error("system happen error {}, requestParam : {}", requestUrl, requestParam, throwable);
            throw throwable;
        } finally {
            long costTime = stopWatch.getTotalTimeMillis();
            log.info("requestUrl = {}, consume : {} ms", requestUrl, costTime);
            log.info("======================= {} end =======================", fullMethodName);
        }
    }

    private static void printResponseBody(Object result) {
        if(Objects.isNull(result)) {
            log.info("request_responseBody={}", "响应报文为空");
            return;
        }

        // 文件流不打印响应报文
        if (result instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            HttpHeaders headers = responseEntity.getHeaders();
            MediaType contentType = headers.getContentType();
            if (Objects.nonNull(contentType) && MediaType.APPLICATION_OCTET_STREAM.getType().equals(contentType.getType())) {
                log.info("request_responseBody={}", "响应报文为文件流, 不打印响应报文");
            } else {
                log.info("request_responseBody={}", JsonTool.toJSONString(result));
            }
            return;
        }

        // 数组不打印响应报文
        if (result.getClass().isArray()) {
            log.info("request_responseBody={}", "响应报文为数组, 不打印响应报文");
            return;
        }

        log.info("request_responseBody={}", JsonTool.toJSONString(result));
    }

    private static Object collectRequestBodyParam(Object[] args, Signature signature) {
        Method method = ((MethodSignature) signature).getMethod();
        Parameter[] parameters = method.getParameters();
        return Arrays.stream(parameters)
                .filter(parameter -> null != parameter.getAnnotation(RequestBody.class))
                .map(parameter -> ArrayUtils.indexOf(parameters, parameter))
                .map(index -> args[index])
                .findFirst()
                .orElse(null);
    }

    private static String getParamStringFromRequest(final HttpServletRequest request) {
        return parseEnumerationToString(request.getParameterNames(), request::getParameter);
    }

    private static String getHeaderStringFromRequest(final HttpServletRequest request) {
        return parseEnumerationToString(request.getHeaderNames(), request::getHeader);
    }

    private static String parseEnumerationToString(Enumeration<String> enumerations, Function<String, String> function) {
        List<String> list = ListUtils.emptyIfNull(EnumerationUtils.toList(enumerations));
        Map<String, String> map = list.stream()
                .collect(Collectors.toMap(Function.identity(), function));
        return Joiner.on("&")
                .withKeyValueSeparator("=")
                .join(map);
    }


}
