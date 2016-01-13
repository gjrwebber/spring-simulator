package org.springframework.simulator.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.simulator.annotation.Simulate;
import org.springframework.simulator.annotation.SimulateResult;

/**
 * Created by gman on 7/01/16.
 */
public abstract aspect AbstractSimulateResultAspect extends AbstractSimulateAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSimulateResultAspect.class);

    @Override
    public Object replay(String key, MethodSignature methodSignature, ProceedingJoinPoint pjp) throws Throwable {

        if (!methodSignature.getMethod().isAnnotationPresent(SimulateResult.class)) {
            return pjp.proceed();
        }

        SimulateResult simulateResult = methodSignature.getMethod().getDeclaredAnnotation(SimulateResult.class);
        Object result = null;
        if (loggerService.isLogger(key)) {
            result = loggerService.getResult(pjp.getArgs(), simulateResult, key);
        }

        if (result == null && simulateResult.getClass().getDeclaredAnnotation(Simulate.class).mockIfNotAvailable()) {
            LOGGER.warn("CaptureResult.mockIfNotAvailable() not yet implemented.");
        }

        return result;
    }
}
