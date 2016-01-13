package org.springframework.simulator.aspectj;

/**
 * Concrete Aspect for {@link org.springframework.simulator.annotation.SimulateResult @SimulateResult} simulation support.
 * It sets the {@code pointcut} to include executions of methods with {@code Object} return type annotated with
 * {@link org.springframework.simulator.annotation.SimulateResult @SimulateResult}.
 *
 * @author gman
 * @see org.springframework.simulator.annotation.SimulateCall
 */
public aspect SimulateResultAnnotationAspect extends AbstractSimulateResultAspect {

    private pointcut simulateResultMarkedMethod(): execution(@SimulateResult * *(..));

    public pointcut simulation(): simulateResultMarkedMethod();

    declare error:
            execution(@SimulateResult * *(..)):
            "Only methods that return something may have a @SimulateResult annotation";


}
