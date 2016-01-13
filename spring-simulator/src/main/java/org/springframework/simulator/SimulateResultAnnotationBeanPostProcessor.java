package org.springframework.simulator;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * Created by gman on 7/01/16.
 */
public class SimulateResultAnnotationBeanPostProcessor extends AbstractSimulateBeanPostProcessor {

    @Autowired
    private LoggerService loggerService;

    public SimulateResultAnnotationBeanPostProcessor(Environment environment) {
        super(environment);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        SimulateResultAnnotationAdvisor advisor = new SimulateResultAnnotationAdvisor(loggerService, simulationMode);
        this.advisor = advisor;
    }

}
