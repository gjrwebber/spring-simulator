package org.springframework.simulator;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.simulator.annotation.Simulate;
import org.springframework.simulator.annotation.SimulateResult;

import java.util.Arrays;

/**
 * Created by gman on 6/01/16.
 */
public class SimulateResultAnnotationInterceptor extends AbstractSimulateAnnotationInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulateResultAnnotationInterceptor.class);

    public SimulateResultAnnotationInterceptor(LoggerService loggerService, SimulationMode simulationMode) {
        super(loggerService, simulationMode);
    }

    @Override
    protected Object replay(MethodInvocation invocation) throws Throwable {

        if (!invocation.getMethod().isAnnotationPresent(SimulateResult.class)) {
            return invocation.proceed();
        }

        SimulateResult simulateResult = invocation.getMethod().getDeclaredAnnotation(SimulateResult.class);

        String key = loggerService.getKey(invocation);

        LOGGER.info("Replaying call to " + key + " with algorithm: " + simulateResult.returnAlgorithm() + " for Args: " + Arrays.toString(invocation.getArguments()));

        Object result = loggerService.getResult(invocation.getArguments(), simulateResult, key);

        if (result == null && simulateResult.getClass().isAnnotationPresent(Simulate.class) && simulateResult.getClass().getAnnotation(Simulate.class).mockIfNotAvailable()) {
            LOGGER.warn("Capture.mockIfNotAvailable() not yet implemented.");
        }

        return result;
    }
}
