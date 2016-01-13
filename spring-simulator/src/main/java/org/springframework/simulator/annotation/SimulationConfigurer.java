package org.springframework.simulator.annotation;

/**
 * Interface to be implemented by @{@link org.springframework.context.annotation.Configuration
 * Configuration} classes annotated with @{@link EnableSimulation} that wish to customise the
 *
 * @author Gman
 * @{@link SimulateCall} or @{@SimulateResult} instance used when processing async method invocations.
 * <p>
 * <p>See @{@link EnableSimulation} for usage examples.
 * @see AbstractSimulationConfiguration
 * @see EnableSimulation
 */
public interface SimulationConfigurer { }
