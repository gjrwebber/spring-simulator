package org.springframework.simulator.sample;

import org.springframework.simulator.annotation.EnableSimulation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ScheduledExecutorTask;

@SpringBootApplication
@EnableSimulation
@EnableLoadTimeWeaving
@EnableAsync
@Configuration
public class SpringReplaySampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringReplaySampleApplication.class, args);
    }

    @Bean
    ScheduledExecutorTask scheduledExecutorTask() {
        return new ScheduledExecutorTask();
    }

    @Bean
    public InstrumentationLoadTimeWeaver loadTimeWeaver() throws Throwable {
        InstrumentationLoadTimeWeaver loadTimeWeaver = new InstrumentationLoadTimeWeaver();
        return loadTimeWeaver;
    }
}
