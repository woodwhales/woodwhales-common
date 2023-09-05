package cn.woodwhales.common.aspect;

import cn.hutool.core.lang.UUID;
import cn.woodwhales.common.util.IpTool;
import cn.woodwhales.common.util.JsonTool;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author woodwhales on 2023-07-27 11:57
 */
@Slf4j
public class AspectTool {

    /**
     * aop 切面
     * @param joinPoint 切入点
     * @return 执行业务请求的响应结果
     * @throws Throwable 业务异常
     */
    public static Object aspect(ProceedingJoinPoint joinPoint) throws Throwable {
        return aspect(joinPoint, null, requestDto -> {
            // 发送业务报警
        });
    }

    /**
     * aop 切面
     * @param joinPoint 切入点
     * @param getTraceIdFunction 获取 traceId 接口
     * @param systemErrorProcessConsumer 接口异常回调函数
     * @return 执行业务请求的响应结果
     * @throws Throwable 业务异常
     */
    public static Object aspect(ProceedingJoinPoint joinPoint,
                                Function<RequestDto, String> getTraceIdFunction,
                                Consumer<RequestDto> systemErrorProcessConsumer) throws Throwable {
        RequestDto requestDto = new RequestDto(joinPoint) {
            @Override
            public void systemErrorProcess() {
                if (Objects.nonNull(systemErrorProcessConsumer)) {
                    systemErrorProcessConsumer.accept(this);
                }
            }
        };
        requestDto.getTraceIdFunction = getTraceIdFunction;
        return requestDto.execute();
    }


    public static class RequestDto {
        /**
         * 请求头携带 key 为 Trace-Id
         */
        public static final String TRACE_ID_HEADER = "Trace-Id";

        public ProceedingJoinPoint joinPoint;

        public StopWatch stopWatch;

        /**
         * 执行类和方法名称
         */
        public String fullMethodName;

        /**
         * 总请求响应耗时
         */
        public long costTime;

        public HttpServletRequest request;

        public HttpServletResponse response;

        /**
         * 请求流水号
         */
        public String traceId;

        /**
         * 请求者IP
         */
        public String clientIpAddress;

        /**
         * 是否为请求触发
         */
        public boolean fromRequest;

        /**
         * 请求方法
         */
        public String requestMethodType;

        /**
         * 请求链接
         */
        public String requestUrl;

        /**
         * 请求报文
         */
        public String requestBody;
        /**
         * 请求参数
         */
        public String requestParam;
        /**
         * 响应报文
         */
        public String responseBody;

        /**
         * 是否忽略切面拦截
         */
        public boolean ignoreAspect;

        public RequestDto(ProceedingJoinPoint joinPoint) {
            this.joinPoint = joinPoint;
            Signature signature = joinPoint.getSignature();
            Class declaringType = signature.getDeclaringType();
            if(declaringType.isAnnotationPresent(IgnoreAspect.class)) {
                this.ignoreAspect = true;
                return;
            } else {
                Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
                if (method.isAnnotationPresent(IgnoreAspect.class)) {
                    this.ignoreAspect = true;
                }
            }

            this.stopWatch = new StopWatch();
            this.start();
            this.fullMethodName = declaringType.getName() + "#" + signature.getName();
            this.fromRequest = Objects.nonNull(RequestContextHolder.getRequestAttributes());
            this.printTraceId();
            this.printRequestParam();
        }

        public void systemErrorProcess() {

        }

        /**
         * 获取 traceId 接口
         */
        public Function<RequestDto, String> getTraceIdFunction;

        private void printTraceId() {
            // 如果有自定义获取 traceId 接口则执行自定义获取 traceId 接口
            if(Objects.nonNull(getTraceIdFunction)) {
                getTraceIdFunction.apply(this);
            } else {
                // http 请求则从请求头中获取
                if(fromRequest) {
                    request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
                    response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
                    this.traceId = request.getHeader(TRACE_ID_HEADER);
                    if(StringUtils.isBlank(this.traceId)) {
                        this.traceId = UUID.randomUUID().toString(true);
                    }
                    request.setAttribute(TRACE_ID_HEADER, this.traceId);
                } else {
                    this.traceId = UUID.randomUUID().toString(true);
                }
                Thread.currentThread().setName(this.traceId);
            }
        }

        private void printRequestParam() {
            StringBuilder requestParamBuilder = new StringBuilder();
            if (fromRequest) {
                this.clientIpAddress = IpTool.getIpAddress(request);
                this.requestMethodType = request.getMethod();
                this.requestUrl = String.format("[ %s ] %s", requestMethodType, request.getRequestURL().toString());
                Object requestBodyParam = collectRequestBodyParam(joinPoint.getArgs(), joinPoint.getSignature());
                if (null != requestBodyParam) {
                    requestParamBuilder.append(JsonTool.toJSONString(requestBodyParam));
                    this.requestBody = requestParamBuilder.toString();
                }
                this.requestParam = getParamStringFromRequest(this.request);
            } else {
                Object[] args = this.joinPoint.getArgs();
                this.requestBody = JsonTool.toJSONString(args);
            }
        }

        public void start() {
            stopWatch.start();
        }

        public void stop() {
            stopWatch.stop();
            this.costTime = stopWatch.getTotalTimeMillis();
        }

        public void fillResponseHeader() {
            if(Objects.nonNull(this.response)) {
                this.response.addHeader(TRACE_ID_HEADER, traceId);
            }
        }

        public Object execute() throws Throwable {
            if(this.ignoreAspect) {
                return this.joinPoint.proceed();
            }

            Object result = null;
            boolean systemError = false;
            Throwable throwable = null;
            try {
                log.info(">>>>>>>>>> start {}, traceId={}", this.fullMethodName, this.traceId);
                if(fromRequest) {
                    log.info("X-AUTH-TOKEN={}", this.request.getHeader("X-AUTH-TOKEN"));
                }
                result = this.joinPoint.proceed();
                return result;
            } catch (Throwable e) {
                systemError = true;
                throwable = e;
                throw e;
            } finally {
                this.printResponseBody(result);
                this.fillResponseHeader();
                this.stop();
                if (systemError) {
                    String errorMsg;
                    if(throwable instanceof NullPointerException) {
                        errorMsg = "NPE";
                    } else {
                        errorMsg = throwable.getMessage();
                    }
                    if(this.fromRequest) {
                        log.error("异常异常! errorMsg={}, requestUrl={}, requestBody={}, consume={}ms", errorMsg, this.requestUrl, this.requestBody, this.costTime, throwable);
                    } else {
                        log.error("异常异常! errorMsg={}, requestBody={}, consume={}ms", errorMsg, this.requestBody, this.costTime, throwable);
                    }

                } else {
                    if(this.fromRequest) {
                        log.info("clientIP={}, requestUrl={}, requestBody={}, responseBody={}, consume={}ms", this.clientIpAddress, this.requestUrl, this.requestBody, this.responseBody, this.costTime);
                    } else {
                        log.info("requestBody={}, responseBody={}, consume={}ms", this.requestBody, this.responseBody, this.costTime);
                    }
                }

                if(systemError) {
                    // 接口异常处理逻辑
                    this.systemErrorProcess();
                }
                log.info("<<<<<<<<<< end {}, traceId={}, consume={}ms", this.fullMethodName, this.traceId, this.costTime);
            }
        }

        private void printResponseBody(Object result) {
            if(Objects.isNull(result)) {
                return;
            }
            if(result instanceof Mono) {
                (((Mono)result)).subscribe(resp -> {
                    if(resp instanceof ResponseEntity) {
                        ResponseEntity responseEntity = (ResponseEntity) resp;
                        HttpHeaders headers = responseEntity.getHeaders();
                        MediaType contentType = headers.getContentType();
                        if(Objects.nonNull(contentType) && MediaType.APPLICATION_OCTET_STREAM.getType().equals(contentType.getType())) {
                            this.responseBody = "文件流不打印响应日志";
                        } else {
                            Object body = ((ResponseEntity) resp).getBody();
                            this.responseBody = JsonTool.toJSONString(body);
                        }
                    }
                });
            } else {
                this.responseBody = JsonTool.toJSONString(result);
            }
        }
    }

    private static Object collectRequestBodyParam(Object[] args, Signature signature) {
        Method method = ((MethodSignature)signature).getMethod();
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
