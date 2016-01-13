package org.springframework.simulator.aspectj;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.simulator.SimulationMode;
import org.springframework.simulator.annotation.AbstractSimulationConfiguration;

/**
 * Created by gman on 7/01/16.
 */
@Configuration
public class AspectjSimulateConfiguration extends AbstractSimulationConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public SimulateCallAnnotationAspect simulateCallAdvisor() {
        SimulateCallAnnotationAspect simulateCallAspect = SimulateCallAnnotationAspect.aspectOf();
        simulateCallAspect.setLoggerService(loggerService());
        simulateCallAspect.setSimulationMode(SimulationMode.fromEnvironment(environment));
        return simulateCallAspect;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public SimulateResultAnnotationAspect simulateResultAdvisor() {
        SimulateResultAnnotationAspect simulateResultAspect = SimulateResultAnnotationAspect.aspectOf();
        simulateResultAspect.setLoggerService(loggerService());
        simulateResultAspect.setSimulationMode(SimulationMode.fromEnvironment(environment));
        return simulateResultAspect;
    }
}
