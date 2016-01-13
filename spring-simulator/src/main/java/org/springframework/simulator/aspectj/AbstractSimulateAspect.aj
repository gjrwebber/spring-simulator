package org.springframework.simulator.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.simulator.LoggerService;
import org.springframework.simulator.RecordedMethod;
import org.springframework.simulator.RecordedMethodLogger;
import org.springframework.simulator.SimulationMode;

/**
 * Created by gman on 7/01/16.
 */
public abstract aspect AbstractSimulateAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSimulateAspect.class);

    protected LoggerService loggerService;
    protected SimulationMode simulationMode;

    @SuppressAjWarnings("adviceDidNotMatch")
    Object around(): simulation() {
        final MethodSignature methodSignature = (MethodSignature) thisJoinPointStaticPart.getSignature();
        final ProceedingJoinPoint pjp = (ProceedingJoinPoint) thisJoinPoint;

        Object result = null;

        String key = loggerService.getKey(pjp.getTarget().getClass(), pjp.getSignature().getName(), pjp.getArgs());
        try {
            if (simulationMode.isSimulating() && methodSignature.getReturnType() != Void.class) {
                result = replay(key, methodSignature, pjp);
            } else if (simulationMode.isRecording()) {

                result = pjp.proceed();
                RecordedMethodLogger logger = loggerService.getLogger(key);
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

    public void setLoggerService(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    public void setSimulationMode(SimulationMode simulationMode) {
        this.simulationMode = simulationMode;
    }
}
