package org.springframework.simulator;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gman on 6/01/16.
 */
public class SimulateCallAnnotationInterceptor extends AbstractSimulateAnnotationInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulateCallAnnotationInterceptor.class);

    public SimulateCallAnnotationInterceptor(LoggerService loggerService, SimulationMode simulationMode) {
        super(loggerService, simulationMode);
    }

    @Override
    protected Object replay(MethodInvocation invocation) throws Throwable {
        LOGGER.info("We are simulating. Ignoring this invocation.");
        return null;
    }
}
