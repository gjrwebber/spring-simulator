package org.springframework.simulator;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.simulator.annotation.SimulateResult;

/**
 * Advisor that activates simulation through the {@link SimulateResult @SimulateResult}
 * annotation at the method level.
 *
 * @author Gman
 * @see SimulateResult
 * @see SimulateResultAnnotationInterceptor
 */
public class SimulateResultAnnotationAdvisor extends AbstractPointcutAdvisor {

    private Pointcut pointcut;
    private Advice advice;

    private RecordedMethodLoggerSupport recordedMethodLoggerSupport;
    private SimulationMode simulationMode;

    public SimulateResultAnnotationAdvisor(RecordedMethodLoggerSupport recordedMethodLoggerSupport, SimulationMode simulationMode) {
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
        advice = new SimulateResultAnnotationInterceptor(recordedMethodLoggerSupport, simulationMode);
    }

    protected void buildPointcut() {
        pointcut = AnnotationMatchingPointcut.forMethodAnnotation(SimulateResult.class);
    }

}
