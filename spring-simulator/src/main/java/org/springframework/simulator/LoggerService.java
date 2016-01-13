package org.springframework.simulator;

import org.aopalliance.intercept.MethodInvocation;
import org.gw.objectlogger.FileSystemDataSource;
import org.gw.objectlogger.IDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.simulator.annotation.Simulate;
import org.springframework.simulator.annotation.SimulateResult;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by gman on 11/01/16.
 */
public class LoggerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerService.class);

    private Map<String, RecordedMethodLogger> loggerCache = new HashMap<>();

    public RecordedMethodLogger getLogger(String key) {
        RecordedMethodLogger logger = loggerCache.get(key);
        if (logger == null) {
            FileSystemDataSource dataSource = new FileSystemDataSource(key.replaceAll("\\.", "_"));
            logger = new RecordedMethodLogger(dataSource);
            loggerCache.put(key, logger);
        }
        return logger;
    }

    public String getKey(MethodInvocation invocation) {

        Class targetClass = invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null;
        Method specificMethod = ClassUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

        String key = getKey(targetClass, specificMethod.getName(), invocation.getArguments());
        return key;
    }

    public String getKey(Class<?> clazz, String methodName, Object... args) {
        Class<?>[] classes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            classes[i] = ClassUtils.resolvePrimitiveIfNecessary(args[i].getClass());
        }
        return getKey(clazz, methodName, classes);
    }

    public String getKey(Class<?> clazz, String methodName, Class<?>... args) {
        StringBuilder builder = new StringBuilder();
        builder.append(clazz.getCanonicalName()).append(".").append(methodName).append("(");
        for (Class<?> argClass : args) {
            builder.append(ClassUtils.resolvePrimitiveIfNecessary(argClass).getName()).append(",");
        }
        if (args.length > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append(")");
        return builder.toString();
    }

    public boolean isLogger(String key) {
        return loggerCache.containsKey(key);
    }


    private Random random = new Random();

    private Map<List<RecordedObject>, List<RecordedMethod>> cache = new HashMap<>();

    public Object getResult(Object[] args, SimulateResult simulateResult, String key) throws Throwable {

        List<RecordedObject> recordedArgs = getRecordedArgs(args);
        List<RecordedMethod> cached = cache.get(recordedArgs);
        if (cached == null) {

            cached = new ArrayList<>();
            cache.put(recordedArgs, cached);

            RecordedMethodLogger logger = getLogger(key);
            IDataSource dataSource = logger.getDataSource();
            if (dataSource != null && !dataSource.isEmpty(RecordedMethod.class)) {
                List<RecordedMethod> capturedList = dataSource.getAll(RecordedMethod.class).asList();
                for (RecordedMethod recordedMethod : capturedList) {
                    if (recordedMethod.getArgs().equals(recordedArgs)) {
                        cached.add(recordedMethod);
                    }
                }
            }
        }

        Object result = null;
        if (cached.size() > 0) {
            RecordedMethod recordedMethod;
            switch (simulateResult.returnAlgorithm()) {
            case FIRST:
                recordedMethod = cached.get(0);
                break;
            case LAST:
                recordedMethod = cached.get(cached.size() - 1);
                break;
            case RANDOM:
                recordedMethod = cached.get(random.nextInt(cached.size()));
                break;
            default:
                throw new IllegalStateException("Unknown return algorithm: " + simulateResult.returnAlgorithm());
            }
            result = recordedMethod.getResult().getValue();
        }

        if (result == null) {
            LOGGER.info("No result captured for " + key + " with args: " + recordedArgs);
            if (simulateResult.getClass().isAnnotationPresent(Simulate.class) && simulateResult.getClass().getAnnotation(Simulate.class).mockIfNotAvailable()) {
                LOGGER.warn("Capture.mockIfNotAvailable() not yet implemented.");
            }
        }


        return result;
    }

    private List<RecordedObject> getRecordedArgs(Object[] args) {
        List<RecordedObject> recordedArgs = new ArrayList<>();
        for (Object arg : args) {
            recordedArgs.add(new RecordedObject(ClassUtils.resolvePrimitiveIfNecessary(arg.getClass()), arg));
        }
        return recordedArgs;
    }

}
