package org.springframework.simulator.aspectj;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.simulator.SimulationMode;
import org.springframework.simulator.annotation.AbstractSimulationConfiguration;
import org.springframework.simulator.annotation.EnableSimulation;

/**
 * Concrete {@code Configuration} class uses AspectJ for enhancing beans for
 * Spring's simulated execution capability.
 *
 * @author Gman
 * @see EnableSimulation
 */
@Configuration
public class AspectjSimulateConfiguration extends AbstractSimulationConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public SimulateCallAnnotationAspect simulateCallAdvisor() {
        SimulateCallAnnotationAspect simulateCallAspect = SimulateCallAnnotationAspect.aspectOf();
        simulateCallAspect.setRecordedMethodLoggerSupport(loggerService());
        simulateCallAspect.setSimulationMode(SimulationMode.fromEnvironment(environment));
        return simulateCallAspect;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public SimulateResultAnnotationAspect simulateResultAdvisor() {
        SimulateResultAnnotationAspect simulateResultAspect = SimulateResultAnnotationAspect.aspectOf();
        simulateResultAspect.setRecordedMethodLoggerSupport(loggerService());
        simulateResultAspect.setSimulationMode(SimulationMode.fromEnvironment(environment));
        return simulateResultAspect;
    }
}
