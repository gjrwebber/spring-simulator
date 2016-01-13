package org.springframework.simulator;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;

/**
 * AOP Alliance {@code MethodInterceptor} that processes method invocations
 * by either recording them or replaying them depending on the {@link SimulationMode}.
 * *
 * @author Gman
 * @see SimulationMode
 * @see org.springframework.simulator.annotation.SimulateCall
 * @see org.springframework.simulator.annotation.SimulateResult
 * @see SimulateCallAnnotationInterceptor
 * @see SimulateResultAnnotationInterceptor
 */
public abstract class AbstractSimulateAnnotationInterceptor implements MethodInterceptor, Ordered {

    protected RecordedMethodLoggerSupport recordedMethodLoggerSupport;
    protected SimulationMode simulationMode;

    public AbstractSimulateAnnotationInterceptor(RecordedMethodLoggerSupport recordedMethodLoggerSupport, SimulationMode simulationMode) {
        this.recordedMethodLoggerSupport = recordedMethodLoggerSupport;
        this.simulationMode = simulationMode;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result;
        if (simulationMode.isSimulating()) {
            result = replay(invocation);
        } else if (simulationMode.isRecording()) {
            result = invocation.proceed();
            String key = recordedMethodLoggerSupport.getKey(invocation);
            RecordedMethodLogger logger = recordedMethodLoggerSupport.getLogger(key);
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
