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
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
     * 请求 traceId
     */
    public static final String TRACE_ID = "trace_id";

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
                    request.setAttribute(TRACE_ID, Thread.currentThread().getName());
                }, null, null);
    }

    /**
     * 对请求controller进行切面
     *
     * @param joinPoint         joinPoint
     * @param traceIdSupplier 请求流水号生成接口
     * @param beforeRequestConsumer    请求执行之前的接口
     * @param afterResponseConsumer     请求执行之后的接口
     * @param finalConsumer     finally 代码块中的回调函数
     * @return 请求执行结果
     * @throws Throwable 异常对象
     */
    public static Object aspect(ProceedingJoinPoint joinPoint,
                                Supplier<String> traceIdSupplier,
                                Consumer<HttpServletRequest> beforeRequestConsumer,
                                BiConsumer<HttpServletRequest, Object> afterResponseConsumer,
                                Consumer<RequestDto> finalConsumer) throws Throwable {
        RequestDto requestDto = new RequestDto();
        StopWatch stopWatch = new StopWatch();
        String fullMethodName = null;
        String traceId = null;
        // 获取 traceId 并设置到当前线程名中
        if (Objects.nonNull(traceIdSupplier)) {
            traceId = traceIdSupplier.get();
            if (StringUtils.isBlank(traceId)) {
                traceId = UUID.randomUUID().toString();
            }
        } else {
            traceId = UUID.randomUUID().toString();
        }

        requestDto.traceId = traceId;
        Thread.currentThread().setName(traceId);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        try {
            stopWatch.start();
            Signature signature = joinPoint.getSignature();
            String declaringTypeName = signature.getDeclaringType().getName();
            fullMethodName = declaringTypeName + "#" + signature.getName();
            log.info("======================= {} start =======================", fullMethodName);
            // 请求ip
            requestDto.clientIpAddress = IpTool.getIpAddress(request);

            // 请求url
            String methodType = request.getMethod();
            requestDto.requestUrl = String.format("[ %s ] %s", methodType, request.getRequestURL().toString());

            // 请求参数
            requestDto.requestParam = getParamStringFromRequest(request);

            // 请求报文
            if (!StringUtils.equals(RequestMethod.GET.name(), methodType)) {
                Object requestBodyParam = collectRequestBodyParam(joinPoint.getArgs(), signature);
                requestDto.requestBody = JsonTool.toJSONString(requestBodyParam);
            }
            //请求头信息
            if (Objects.nonNull(beforeRequestConsumer)) {
                beforeRequestConsumer.accept(request);
            }

            log.info("请求url：{}", requestDto.requestUrl);
            log.info("请求ip：{}", requestDto.clientIpAddress);
            log.info("请求参数：{}", requestDto.requestParam);

            Object result = joinPoint.proceed();

            // 响应报文
            printResponseBody(result, requestDto);

            log.info("响应报文：{}", requestDto.requestBody);
            if (Objects.nonNull(afterResponseConsumer)) {
                afterResponseConsumer.accept(request, result);
            }
            return result;
        } catch (Throwable throwable) {
            log.error("请求响应异常, errorMsg={}, 请求url：{}, 请求参数：{}, 请求报文：{}",
                    throwable.getMessage(), requestDto.requestUrl, requestDto.requestParam, requestDto.requestBody, throwable);
            requestDto.throwable = throwable;
            throw throwable;
        } finally {
            requestDto.costTime = stopWatch.getTotalTimeMillis();
            log.info("请求url：{}, 响应耗时：{} ms", requestDto.requestUrl, requestDto.costTime);
            if(Objects.nonNull(finalConsumer)) {
                finalConsumer.accept(requestDto);
            }
            log.info("======================= {} end =======================", fullMethodName);
            // 响应头中增加 traceId
            response.addHeader(TRACE_ID, requestDto.traceId);
        }
    }

    private static void printResponseBody(Object result, RequestDto requestDto) {
        if(Objects.isNull(result)) {
            requestDto.requestBody = null;
        }

        if(result instanceof Mono) {
            (((Mono)result)).subscribe(resp -> {
                if(resp instanceof ResponseEntity) {
                    ResponseEntity responseEntity = (ResponseEntity) resp;
                    HttpHeaders headers = responseEntity.getHeaders();
                    MediaType contentType = headers.getContentType();
                    if(Objects.nonNull(contentType) && MediaType.APPLICATION_OCTET_STREAM.getType().equals(contentType.getType())) {
                        requestDto.requestBody = "文件流不打印响应日志";
                    } else {
                        requestDto.requestBody = JsonUtils.toJson(responseEntity.getBody());
                    }
                } else {
                    requestDto.requestBody = JsonUtils.toJson(resp);
                }
            });
        } else if (result instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            HttpHeaders headers = responseEntity.getHeaders();
            MediaType contentType = headers.getContentType();
            // 文件流不打印响应报文
            if (Objects.nonNull(contentType) && MediaType.APPLICATION_OCTET_STREAM.getType().equals(contentType.getType())) {
                requestDto.requestBody = "响应报文为文件流, 不打印响应报文";
                return;
            } else {
                requestDto.requestBody = JsonUtils.toJson(result);
            }
        }

        // 数组不打印响应报文
        if (result.getClass().isArray()) {
            requestDto.requestBody = "响应报文为数组, 不打印响应报文";
        } else {
            requestDto.requestBody = JsonUtils.toJson(result);
        }
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

    public static class RequestDto {
        /**
         * 请求ip
         */
        String clientIpAddress;
        /**
         * 请求url
         */
        String requestUrl;
        /**
         * traceId
         */
        String traceId;
        /**
         * 请求参数
         */
        String requestParam;
        /**
         * 请求报文
         */
        String requestBody;
        /**
         * 本次请求响应的耗时，单位：ms
         */
        long costTime;
        /**
         * 本次请求发生异常，如果发生异常，则不为null
         */
        Throwable throwable;
    }

}
