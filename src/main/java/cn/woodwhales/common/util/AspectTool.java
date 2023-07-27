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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author woodwhales on 2023-07-27 11:57
 */
@Slf4j
public class AspectTool {

    public static Object aspect(ProceedingJoinPoint joinPoint) throws Throwable {
        return aspect(joinPoint, null, requestDto -> {
            // 发送业务报警
        });
    }

    /**
     *
     * @param joinPoint
     * @param systemErrorProcessConsumer 接口异常回调函数
     * @return
     * @throws Throwable
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

        public static final String TRACE_ID_HEADER = "Trace_Id";


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
         * 响应报文
         */
        public String responseBody;

        public RequestDto(ProceedingJoinPoint joinPoint) {
            this.stopWatch = new StopWatch();
            this.start();
            this.joinPoint = joinPoint;
            Signature signature = joinPoint.getSignature();
            String declaringTypeName = signature.getDeclaringType().getName();
            this.fullMethodName = declaringTypeName + "#" + signature.getName();
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
                if(!StringUtils.equals(RequestMethod.GET.name(), this.requestMethodType)) {
                    Object requestBodyParam = collectRequestBodyParam(joinPoint.getArgs(), joinPoint.getSignature());
                    if (null != requestBodyParam) {
                        requestParamBuilder.append(JsonTool.toJSONString(requestBodyParam));
                        this.requestBody = requestParamBuilder.toString();
                    }
                } else {
                    this.requestBody = getParamStringFromRequest(this.request);
                }
            } else {
                Object[] args = this.joinPoint.getArgs();
                if(Objects.nonNull(args)) {
                    Map<String, Object> map = new HashMap<>();
                    int index = 1;
                    for (Object arg : args) {
                        map.put("param" + index, arg);
                        index++;
                    }
                    requestParamBuilder.append(JsonTool.toJSONString(map));
                }
                this.requestBody = requestParamBuilder.toString();
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

        public Object execute() {
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
                throw new RuntimeException(e);
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
