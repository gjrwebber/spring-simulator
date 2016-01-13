package org.springframework.simulator.aspectj;

/**
 * Concrete Aspect for {@link org.springframework.simulator.annotation.SimulateCall @SimulateCall} simulation support.
 * It sets the {@code pointcut} to include executions of methods with {@code void} return type annotated with
 * {@link org.springframework.simulator.annotation.SimulateCall @SimulateCall}.
 *
 * @author gman
 * @see org.springframework.simulator.annotation.SimulateCall
 */
public aspect SimulateCallAnnotationAspect extends AbstractSimulateCallAspect {

    private pointcut simulateCallMarkedMethod(): execution(@SimulateCall (void) *(..));

    public pointcut simulation(): simulateCallMarkedMethod();

    declare error:
            execution(@SimulateCall (void) *(..)):
            "Only methods that return void may have a @SimulateCall annotation";

}
