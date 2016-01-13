package org.springframework.simulator;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.simulator.annotation.SimulateCall;

/**
 * Created by gman on 6/01/16.
 */
public class SimulateCallAnnotationAdvisor extends AbstractPointcutAdvisor {

    private Pointcut pointcut;
    private Advice advice;

    private LoggerService loggerService;
    private SimulationMode simulationMode;

    public SimulateCallAnnotationAdvisor(LoggerService loggerService, SimulationMode simulationMode) {
        this.loggerService = loggerService;
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
        advice = new SimulateCallAnnotationInterceptor(loggerService, simulationMode);
    }

    protected void buildPointcut() {
        pointcut = AnnotationMatchingPointcut.forMethodAnnotation(SimulateCall.class);
    }

}
