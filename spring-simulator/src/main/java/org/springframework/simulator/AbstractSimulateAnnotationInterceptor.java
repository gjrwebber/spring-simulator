package org.springframework.simulator;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;

/**
 * Created by gman on 6/01/16.
 */
public abstract class AbstractSimulateAnnotationInterceptor implements MethodInterceptor, Ordered {

    protected LoggerService loggerService;
    protected SimulationMode simulationMode;

    public AbstractSimulateAnnotationInterceptor(LoggerService loggerService, SimulationMode simulationMode) {
        this.loggerService = loggerService;
        this.simulationMode = simulationMode;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result;
        if (simulationMode.isSimulating()) {
            result = replay(invocation);
        } else if (simulationMode.isRecording()) {
            result = invocation.proceed();
            String key = loggerService.getKey(invocation);
            RecordedMethodLogger logger = loggerService.getLogger(key);
            RecordedMethod recordedMethod = new RecordedMethod(result, key, invocation.getArguments());
            logger.log(recordedMethod);
        } else {
            result = invocation.proceed();
        }
        return result;
    }

    protected abstract Object replay(MethodInvocation invocation) throws Throwable;

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
