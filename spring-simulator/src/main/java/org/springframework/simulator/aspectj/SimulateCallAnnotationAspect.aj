package org.springframework.simulator.aspectj;

/**
 * Created by gman on 7/01/16.
 */
public aspect SimulateCallAnnotationAspect extends AbstractSimulateCallAspect {

    private pointcut recordInvocationsMarkedMethod(): execution(@RecordInvocations (void) *(..));

    public pointcut simulation(): recordInvocationsMarkedMethod();

    declare error:
            execution(@RecordInvocations (void) *(..)):
            "Only methods that return void may have a @RecordInvocations annotation";

}
