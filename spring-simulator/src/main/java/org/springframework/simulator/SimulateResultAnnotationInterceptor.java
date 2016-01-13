package org.springframework.simulator;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.simulator.annotation.SimulateResult;

import java.util.Arrays;

/**
 * Concrete {@link AbstractSimulateAnnotationInterceptor} for
 * {@link org.springframework.simulator.annotation.SimulateResult @SimulateResult} simulation support.
 * This simulation method replays the result previously recorded. As many results could've been recorded for set
 * of arguments, the actual result returned is selected based on the {@link SimulateResult#returnAlgorithm()}.
 * <p>
 * There are no constraints on the parameter type for the target method signature.
 * However they all must implement {@code equals()} if the simulation is to
 * accurately represent the method call.
 * <p>
 * E.g. Given the method below:
 * <pre>
 *     {@code @SimulateResult}
 *      String toString(Date date);
 *     </pre>
 * If {@code Date} did not implement {@code equals()} we could not return the result for
 * the particular date instance of the simulated call and the simulation would not be accurate.
 *
 * @author gman
 * @see org.springframework.simulator.annotation.SimulateResult
 * @see AbstractSimulateAnnotationInterceptor
 */
public class SimulateResultAnnotationInterceptor extends AbstractSimulateAnnotationInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulateResultAnnotationInterceptor.class);

    public SimulateResultAnnotationInterceptor(RecordedMethodLoggerSupport recordedMethodLoggerSupport, SimulationMode simulationMode) {
        super(recordedMethodLoggerSupport, simulationMode);
    }

    @Override
    protected Object replay(MethodInvocation invocation) throws Throwable {

        if (!invocation.getMethod().isAnnotationPresent(SimulateResult.class)) {
            return invocation.proceed();
        }

        SimulateResult simulateResult = invocation.getMethod().getDeclaredAnnotation(SimulateResult.class);

        String key = recordedMethodLoggerSupport.getKey(invocation);

        LOGGER.info("Replaying call to " + key + " with algorithm: " + simulateResult.returnAlgorithm() + " for Args: " + Arrays.toString(invocation.getArguments()));

        Object result = recordedMethodLoggerSupport.getResult(invocation.getArguments(), simulateResult, key);

        if (result == null && simulateResult.mockIfNotAvailable()) {
            LOGGER.warn("Capture.mockIfNotAvailable() not yet implemented.");
        }

        return result;
    }
}
