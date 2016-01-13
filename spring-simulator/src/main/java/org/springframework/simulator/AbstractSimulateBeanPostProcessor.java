package org.springframework.simulator;

import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.core.env.Environment;


/**
 * <p>
 * Bean post-processor that sets the {@link SimulationMode} for concrete simulation bean post-processors.
 *
 * @author Gman
 * @see org.springframework.simulator.annotation.SimulateCall
 * @see org.springframework.simulator.annotation.SimulateResult
 * @see SimulateCallAnnotationAdvisor
 * @see SimulateResultAnnotationAdvisor
 */
public abstract class AbstractSimulateBeanPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {

    protected SimulationMode simulationMode;

    public AbstractSimulateBeanPostProcessor(Environment environment) {
        simulationMode = SimulationMode.fromEnvironment(environment);
    }
}
