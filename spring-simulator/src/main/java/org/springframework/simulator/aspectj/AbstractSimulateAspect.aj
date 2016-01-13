package org.springframework.simulator.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.simulator.RecordedMethodLoggerSupport;
import org.springframework.simulator.RecordedMethod;
import org.springframework.simulator.RecordedMethodLogger;
import org.springframework.simulator.SimulationMode;

/**
 * Base Aspect for simulation support. Depending on the current {@link SimulationMode} the advice
 * wraps the method and either records the method invocation details ({@link SimulationMode#isRecording()})
 * using a {@link RecordedMethodLogger} or replays the recorded data ({@link SimulationMode#isSimulating()})
 * by calling the concrete Aspect.
 *
 * @author gman
 * @see SimulationMode
 * @see RecordedMethodLogger
 */
public abstract aspect AbstractSimulateAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSimulateAspect.class);

    protected RecordedMethodLoggerSupport recordedMethodLoggerSupport;
    protected SimulationMode simulationMode;

    @SuppressAjWarnings("adviceDidNotMatch")
    Object around(): simulation() {
        final MethodSignature methodSignature = (MethodSignature) thisJoinPointStaticPart.getSignature();
        final ProceedingJoinPoint pjp = (ProceedingJoinPoint) thisJoinPoint;

        Object result = null;

        String key = recordedMethodLoggerSupport.getKey(pjp.getTarget().getClass(), pjp.getSignature().getName(), pjp.getArgs());
        try {
            if (simulationMode.isSimulating()) {
                result = replay(key, methodSignature, pjp);
            } else if (simulationMode.isRecording()) {

                result = pjp.proceed();
                RecordedMethodLogger logger = recordedMethodLoggerSupport.getLogger(key);
                RecordedMethod call = new RecordedMethod(result, key, pjp.getArgs());
                logger.log(call);
            } else {
                result = pjp.proceed();
            }

        } catch (Throwable throwable) {
            LOGGER.error("Could not execute advice: " + throwable.getMessage(), throwable);
        }
        return result;
    }

    /**
     * Return the set of joinpoints at which simulation advice should be applied.
     */
    public abstract pointcut simulation();

    public abstract Object replay(String key, MethodSignature methodSignature, ProceedingJoinPoint pjp) throws Throwable;

    public void setRecordedMethodLoggerSupport(RecordedMethodLoggerSupport recordedMethodLoggerSupport) {
        this.recordedMethodLoggerSupport = recordedMethodLoggerSupport;
    }

    public void setSimulationMode(SimulationMode simulationMode) {
        this.simulationMode = simulationMode;
    }
}
