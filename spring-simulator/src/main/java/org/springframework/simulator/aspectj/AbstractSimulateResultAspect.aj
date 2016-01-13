package org.springframework.simulator.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.simulator.annotation.SimulateResult;

/**
 * Base Aspect for {@link org.springframework.simulator.annotation.SimulateResult @SimulateResult} simulation support. This
 * simulation method replays the result previously recorded. As many results could've been recorded for set
 * of arguments, the actual result returned is selected based on the {@link SimulateResult#returnAlgorithm()}.
 *
 * @author Gman
 * @see org.springframework.simulator.annotation.SimulateCall
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
        if (recordedMethodLoggerSupport.isLogger(key)) {
            result = recordedMethodLoggerSupport.getResult(pjp.getArgs(), simulateResult, key);
        }

        if (result == null && simulateResult.mockIfNotAvailable()) {
            LOGGER.warn("CaptureResult.mockIfNotAvailable() not yet implemented.");
        }

        return result;
    }
}
