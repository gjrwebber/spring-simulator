package org.springframework.simulator.annotation;

import org.aspectj.lang.Aspects;
import org.gw.commons.aspects.TimeShiftAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.simulator.RecordedMethodLoggerSupport;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collection;


/**
 * Abstract base {@code Configuration} class providing common structure for enabling
 * Spring's simulated execution capability.
 *
 * @author Gman
 * @see EnableSimulation
 */
@Configuration
public class AbstractSimulationConfiguration implements ImportAware, EnvironmentAware {

    protected AnnotationAttributes enableSimulation;

    protected Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableSimulation = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableSimulation.class.getName(), false));
        Assert.notNull(this.enableSimulation, "@EnableSimulation is not present on importing class " + importMetadata.getClassName());
    }

    /**
     * Collect any {@link SimulationConfigurer} beans through autowiring.
     */
    @Autowired(required = false)
    void setConfigurers(Collection<SimulationConfigurer> configurers) {
        if (CollectionUtils.isEmpty(configurers)) {
            return;
        }
        if (configurers.size() > 1) {
            throw new IllegalStateException("Only one SimulationConfigurer may exist");
        }
        SimulationConfigurer configurer = configurers.iterator().next();
        // configurer. Nothing yet.
    }

    @Bean
    protected RecordedMethodLoggerSupport loggerService() {
        return new RecordedMethodLoggerSupport();
    }

    @Bean
    protected TimeShiftAspect timeShiftAspect() {
        return Aspects.aspectOf(TimeShiftAspect.class);
    }
}
