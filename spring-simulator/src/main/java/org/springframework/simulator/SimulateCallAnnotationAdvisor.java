package org.springframework.simulator;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.simulator.annotation.SimulateCall;

/**
 * Advisor that activates simulation through the {@link SimulateCall @SimulateCall}
 * annotation at the method level.
 *
 * @author Gman
 * @see SimulateCall
 * @see SimulateCallAnnotationInterceptor
 */
public class SimulateCallAnnotationAdvisor extends AbstractPointcutAdvisor {

    private Pointcut pointcut;
    private Advice advice;

    private RecordedMethodLoggerSupport recordedMethodLoggerSupport;
    private SimulationMode simulationMode;

    public SimulateCallAnnotationAdvisor(RecordedMethodLoggerSupport recordedMethodLoggerSupport, SimulationMode simulationMode) {
        this.recordedMethodLoggerSupport = recordedMethodLoggerSupport;
        this.simulationMode = simulationMode;
    }

    @Override
    public Pointcut getPointcut() {
        if (pointcut == null) {
            buildPointcut();
        }
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        if (advice == null) {
            buildAdvice();
        }
        return advice;
    }

    protected void buildAdvice() {
        advice = new SimulateCallAnnotationInterceptor(recordedMethodLoggerSupport, simulationMode);
    }

    protected void buildPointcut() {
        pointcut = AnnotationMatchingPointcut.forMethodAnnotation(SimulateCall.class);
    }

}
