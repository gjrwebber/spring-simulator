package org.springframework.simulator;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Concrete {@link AbstractSimulateAnnotationInterceptor} for
 * {@link org.springframework.simulator.annotation.SimulateCall @SimulateCall} simulation support.
 * This simulation method does not replay anything when the method is invoked in simulation. Instead
 * it just logs a message saying as much and returns.
 *
 * @author gman
 * @see org.springframework.simulator.annotation.SimulateCall
 * @see AbstractSimulateAnnotationInterceptor
 */
public class SimulateCallAnnotationInterceptor extends AbstractSimulateAnnotationInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulateCallAnnotationInterceptor.class);

    public SimulateCallAnnotationInterceptor(RecordedMethodLoggerSupport recordedMethodLoggerSupport, SimulationMode simulationMode) {
        super(recordedMethodLoggerSupport, simulationMode);
    }

    @Override
    protected Object replay(MethodInvocation invocation) throws Throwable {
        LOGGER.info("We are simulating. Ignoring this invocation.");
        return null;
    }
}
