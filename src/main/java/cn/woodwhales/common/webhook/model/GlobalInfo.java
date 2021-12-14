package cn.woodwhales.common.webhook.model;

import cn.woodwhales.common.webhook.enums.WebhookProductEnum;
import cn.woodwhales.common.webhook.plugin.WebhookExtraInfo;
import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
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

    private String[] basePackageNames;

    /**
     * 限制栈的内存占用空间大小
     */
    private int LIMIT = 20_000 / 3;

    private WebhookProductEnum webhookProductEnum;

    private String defaultErrorDesc = "异常栈信息太长，不打印全栈日志";

    public GlobalInfo(WebhookProductEnum webhookProductEnum,
                      Throwable throwable,
                      String[] basePackageNames,
                      WebhookExtraInfo webhookExtraInfo) {
        this.webhookProductEnum = webhookProductEnum;
        this.throwable = throwable;
        this.basePackageNames = basePackageNames;
        if(Objects.nonNull(webhookExtraInfo)) {
            this.machineInfoMap = webhookExtraInfo.getMachineInfoMap();
            this.gitProperties = webhookExtraInfo.getGitProperties();
        }
    }

    public void setMachineInfoMap(LinkedHashMap<String, String> machineInfoMap) {
        this.machineInfoMap = machineInfoMap;
    }

    public void setGitProperties(Properties gitProperties) {
        this.gitProperties = gitProperties;
    }

    public void setBasePackageNames(String[] basePackageNames) {
        this.basePackageNames = basePackageNames;
    }

    public List<Pair<String, String>> getAllInfoPair(WebhookProductEnum webhookProductEnum) {
        if(Objects.isNull(this.webhookProductEnum)) {
            this.webhookProductEnum = webhookProductEnum;
        }

        List<Pair<String, String>> allInfoPair = new ArrayList<>();
        this.generateOccurTime(allInfoPair);
        this.generateGitProperties(allInfoPair);
        this.generateMachineInfoMap(allInfoPair);
        this.generateThrowable(allInfoPair);
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
                if (Objects.nonNull(this.basePackageNames)) {
                    final HashSet<String> basePackageNameSet = Sets.newHashSet(this.basePackageNames);
                    List<String> systemStackInfoList = Stream.of(stackTrace)
                            .filter(stackTraceElement -> this.matchBasePackageNameSet(basePackageNameSet, stackTraceElement.getClassName()))
                            .filter(stackTraceElement -> !containsIgnoreCase(stackTraceElement.getClassName(), "$$"))
                            .map(stackTraceElement -> String.format("%s#%s(%s:%d)",
                                    stackTraceElement.getClassName(),
                                    stackTraceElement.getMethodName(),
                                    stackTraceElement.getFileName(),
                                    stackTraceElement.getLineNumber()))
                            .collect(Collectors.toList());

                    if(CollectionUtils.isNotEmpty(systemStackInfoList)) {
                        allInfoPair.add(Pair.of(String.format("本系统 %s 包下异常栈信息：\n\r", Joiner.on(",").join(this.basePackageNames)),
                                                Joiner.on("\n\r").join(systemStackInfoList)));
                    }
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

    private boolean matchBasePackageNameSet(final HashSet<String> basePackageNameSet, String className) {

        final Iterator<String> iterator = basePackageNameSet.iterator();
        while (iterator.hasNext()) {
            final String basePackageName = iterator.next();
            if(StringUtils.containsIgnoreCase(className, basePackageName)) {
                return true;
            }
        }
        return false;
    }

}
