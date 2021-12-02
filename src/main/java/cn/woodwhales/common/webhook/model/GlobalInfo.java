package cn.woodwhales.common.webhook.model;

import cn.woodwhales.common.webhook.enums.WebhookProductEnum;
import com.google.common.base.Throwables;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.time.DateFormatUtils.format;

/**
 * @author woodwhales on 2021-07-20 10:45
 */
public class GlobalInfo {

    public Throwable throwable;

    public LinkedHashMap<String, String> machineInfoMap;

    public Properties gitProperties;

    private String occurTime = format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");

    private String basePackName;

    /**
     * 限制栈的内存占用空间大小
     */
    private int LIMIT = 20_000 / 3;

    private WebhookProductEnum webhookProductEnum;

    private String defaultErrorDesc = "异常栈信息太长，不打印全栈日志";

    public GlobalInfo(WebhookProductEnum webhookProductEnum,
                      Throwable throwable,
                      String basePackName,
                      LinkedHashMap<String, String> machineInfoMap,
                      Properties gitProperties) {
        this.webhookProductEnum = webhookProductEnum;
        this.throwable = throwable;
        this.basePackName = basePackName;
        this.machineInfoMap = machineInfoMap;
        this.gitProperties = gitProperties;
    }

    public GlobalInfo(WebhookProductEnum webhookProductEnum,
                      Throwable throwable,
                      String basePackName) {
        this.webhookProductEnum = webhookProductEnum;
        this.throwable = throwable;
        this.basePackName = basePackName;
    }

    public void setMachineInfoMap(LinkedHashMap<String, String> machineInfoMap) {
        this.machineInfoMap = machineInfoMap;
    }

    public void setGitProperties(Properties gitProperties) {
        this.gitProperties = gitProperties;
    }

    public void setBasePackName(String basePackName) {
        this.basePackName = basePackName;
    }

    public List<Pair<String, String>> getAllInfoPair() {
        List<Pair<String, String>> allInfoPair = new ArrayList<>();
        generateOccurTime(allInfoPair);
        generateGitProperties(allInfoPair);
        generateMachineInfoMap(allInfoPair);
        generateThrowable(allInfoPair);
        return allInfoPair;
    }

    public String getOccurTime() {
        return occurTime;
    }

    private void generateOccurTime(List<Pair<String, String>> allInfoPair) {
        allInfoPair.add(Pair.of("发生时间：", this.occurTime));
    }

    private void generateGitProperties(List<Pair<String, String>> allInfoPair) {
        if(nonNull(this.gitProperties)) {
            allInfoPair.add(Pair.of("部署分支：", this.gitProperties.getProperty("git.branch")));
        }
    }

    private void generateMachineInfoMap(List<Pair<String, String>> allInfoPair) {
        if(nonNull(this.machineInfoMap) && !this.machineInfoMap.isEmpty()) {
            this.machineInfoMap.entrySet().stream().forEach(
                    entry -> allInfoPair.add(Pair.of(entry.getKey(), entry.getValue()))
            );
        }
    }

    private void generateThrowable(List<Pair<String, String>> allInfoPair) {
        if(nonNull(this.throwable)) {
            allInfoPair.add(Pair.of("异常类名：", this.throwable.getClass().getName()));
            String errorMessage = this.throwable.getMessage();

            if(isNotBlank(errorMessage)) {
                allInfoPair.add(Pair.of("异常原因：", errorMessage));
            } else {
                if (this.throwable instanceof NullPointerException) {
                    allInfoPair.add(Pair.of("异常原因：", "空指针异常"));
                } else {
                    allInfoPair.add(Pair.of("异常原因：", "该异常类无errorMessage"));
                }
            }

            StackTraceElement[] stackTrace = this.throwable.getStackTrace();
            if (Objects.nonNull(stackTrace)) {
                List<String> systemStackInfoList = Stream.of(stackTrace)
                        .filter(stackTraceElement ->
                                isNotBlank(this.basePackName) &&
                                        containsIgnoreCase(stackTraceElement.getClassName(), this.basePackName))
                        .filter(stackTraceElement -> !containsIgnoreCase(stackTraceElement.getClassName(), "$$"))
                        .map(stackTraceElement -> String.format("%s#%s(%s:%d)",
                                stackTraceElement.getClassName(),
                                stackTraceElement.getMethodName(),
                                stackTraceElement.getFileName(),
                                stackTraceElement.getLineNumber()))
                        .collect(Collectors.toList());

                if (nonNull(systemStackInfoList) && !systemStackInfoList.isEmpty() && isNotBlank(this.basePackName)) {
                    allInfoPair.add(Pair.of("本系统 ", this.basePackName + " 包下异常栈信息："));
                    systemStackInfoList.stream().forEach(systemStackInfo ->
                            allInfoPair.add(Pair.of("栈信息：", systemStackInfo))
                    );
                }

                String stackTraceAsString = Throwables.getStackTraceAsString(this.throwable);
                int length = StringUtils.length(stackTraceAsString);
                if (this.webhookProductEnum.limitContentLength > length) {
                    allInfoPair.add(Pair.of("全栈异常信息：", stackTraceAsString));
                } else {
                    allInfoPair.add(Pair.of("全栈异常信息：", this.defaultErrorDesc));
                }
            }
        }
    }

}
