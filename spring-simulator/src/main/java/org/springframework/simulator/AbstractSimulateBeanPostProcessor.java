package org.springframework.simulator;

import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.core.env.Environment;

/**
 * Created by gman on 6/01/16.
 */
public class AbstractSimulateBeanPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {

    protected SimulationMode simulationMode;

    public AbstractSimulateBeanPostProcessor(Environment environment) {
        setBeforeExistingAdvisors(false);
        simulationMode = SimulationMode.fromEnvironment(environment);
    }
}
