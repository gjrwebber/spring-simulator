package org.springframework.simulator.annotation;

import org.springframework.simulator.MethodCallSimulator;

/**
 * Interface to be implemented by @{@link org.springframework.context.annotation.Configuration
 * Configuration} classes annotated with @{@link EnableSimulation} that wish to customize the
 * {@link MethodCallSimulator} instance used when processing async method invocations.
 * <p>
 * <p>See @{@link EnableSimulation} for usage examples.
 *
 * @author Gman
 * @see AbstractSimulationConfiguration
 * @see EnableSimulation
 */
public interface SimulationConfigurer { }
