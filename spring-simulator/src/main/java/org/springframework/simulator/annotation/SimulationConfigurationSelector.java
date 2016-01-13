package org.springframework.simulator.annotation;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.simulator.aspectj.AspectjSimulateConfiguration;

/**
 * Selects which implementation of {@link AbstractSimulationConfiguration} should be used based
 * on the value of {@link EnableSimulation#mode} on the importing {@code @Configuration} class.
 *
 * @author Gman
 * @see EnableSimulation
 * @see ProxySimulationConfiguration
 * @see AspectjSimulateConfiguration
 */
public class SimulationConfigurationSelector extends AdviceModeImportSelector<EnableSimulation> {

    /**
     * {@inheritDoc}
     *
     * @return {@link ProxySimulationConfiguration} or {@code AspectjSimulateConfiguration} for
     * {@code PROXY} and {@code ASPECTJ} values of {@link EnableSimulation#mode()}, respectively
     */
    @Override
    public String[] selectImports(AdviceMode adviceMode) {
        switch (adviceMode) {
        case PROXY:
            return new String[]{ ProxySimulationConfiguration.class.getName() };
        case ASPECTJ:
            return new String[]{ AspectjSimulateConfiguration.class.getName() };
        default:
            throw new IllegalStateException("Unknown AdviceMode " + adviceMode + ". Expected one of PROXY or ASPECTJ.");
        }
    }

}
