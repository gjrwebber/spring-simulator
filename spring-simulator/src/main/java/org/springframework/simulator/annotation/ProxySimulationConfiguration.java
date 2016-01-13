package org.springframework.simulator.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.simulator.SimulateCallAnnotationBeanPostProcessor;
import org.springframework.simulator.SimulateResultAnnotationBeanPostProcessor;
import org.springframework.util.Assert;

/**
 * Created by gman on 7/01/16.
 */
@Configuration
public class ProxySimulationConfiguration extends AbstractSimulationConfiguration implements EnvironmentAware {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public SimulateCallAnnotationBeanPostProcessor simulateCallAdvisor() {
        Assert.notNull(this.enableSimulation, "@EnableSimuation annotation metadata was not injected");
        SimulateCallAnnotationBeanPostProcessor bpp = new SimulateCallAnnotationBeanPostProcessor(environment);
        bpp.setProxyTargetClass(this.enableSimulation.getBoolean("proxyTargetClass"));
        bpp.setOrder(this.enableSimulation.<Integer>getNumber("order"));
        return bpp;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public SimulateResultAnnotationBeanPostProcessor simulateResultAdvisor() {
        Assert.notNull(this.enableSimulation, "@EnableSimuation annotation metadata was not injected");
        SimulateResultAnnotationBeanPostProcessor bpp = new SimulateResultAnnotationBeanPostProcessor(environment);
        bpp.setProxyTargetClass(this.enableSimulation.getBoolean("proxyTargetClass"));
        bpp.setOrder(this.enableSimulation.<Integer>getNumber("order"));
        return bpp;
    }


}
