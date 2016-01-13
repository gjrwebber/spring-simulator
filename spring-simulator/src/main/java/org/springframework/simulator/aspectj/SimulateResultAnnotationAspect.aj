package org.springframework.simulator.aspectj;

/**
 * Created by gman on 7/01/16.
 */
public aspect SimulateResultAnnotationAspect extends AbstractSimulateResultAspect {

    private pointcut captureResultMarkedMethod(): execution(@CaptureResult (java.lang.Object) *(..));

    public pointcut simulation(): captureResultMarkedMethod();

    declare error:
            execution(@CaptureResult (java.lang.Object) *(..)):
            "Only methods that return something may have a @CaptureResult annotation";


}
