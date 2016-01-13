package org.springframework.simulator;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * <p>
 * Bean post-processor that automatically applies simulation behaviour
 * to any bean's method that is annotated with
 * {@link org.springframework.simulator.annotation.SimulateResult @SimulateResult}.
 * This is done by adding a corresponding {@link SimulateResultAnnotationAdvisor} to the
 * exposed proxy (either an existing AOP proxy or a newly generated proxy that
 * implements all of the target's interfaces).
 *
 * @author Gman
 * @see org.springframework.simulator.annotation.SimulateResult
 * @see SimulateResultAnnotationAdvisor
 */
public class SimulateResultAnnotationBeanPostProcessor extends AbstractSimulateBeanPostProcessor {

    @Autowired
    private RecordedMethodLoggerSupport recordedMethodLoggerSupport;

    public SimulateResultAnnotationBeanPostProcessor(Environment environment) {
        super(environment);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        SimulateResultAnnotationAdvisor advisor = new SimulateResultAnnotationAdvisor(recordedMethodLoggerSupport, simulationMode);
        this.advisor = advisor;
    }

}
