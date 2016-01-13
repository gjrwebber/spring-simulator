package org.springframework.simulator.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base Aspect for method calling simulation support. This simulation method does not replay
 * anything when the method is invoked in simulation. Instead it just logs a message
 * saying as much and returns.
 *
 * @author gman
 * @see org.springframework.simulator.annotation.SimulateCall
 */
public abstract aspect AbstractSimulateCallAspect extends AbstractSimulateAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSimulateCallAspect.class);

    @Override
    public Object replay(String key, MethodSignature methodSignature, ProceedingJoinPoint pjp) throws Throwable {
        LOGGER.info("We are simulating. Ignoring this invocation.");
        return null;
    }
}