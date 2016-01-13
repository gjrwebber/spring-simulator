package org.springframework.simulator.annotation;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.scheduling.annotation.ProxyAsyncConfiguration;

/**
 * Created by gman on 7/01/16.
 */
public class SimulationConfigurationSelector extends AdviceModeImportSelector<EnableSimulation> {

    private static final String SIMULATION_EASPECT_CONFIGURATION_CLASS_NAME = "org.gw.delorian.aspectj.AspectjSimulationConfiguration";

    /**
     * {@inheritDoc}
     *
     * @return {@link ProxyAsyncConfiguration} or {@code AspectJAsyncConfiguration} for
     * {@code PROXY} and {@code ASPECTJ} values of {@link EnableSimulation#mode()}, respectively
     */
    @Override
    public String[] selectImports(AdviceMode adviceMode) {
        switch (adviceMode) {
        case PROXY:
            return new String[]{ ProxySimulationConfiguration.class.getName() };
        case ASPECTJ:
            return new String[]{ SIMULATION_EASPECT_CONFIGURATION_CLASS_NAME };
        default:
            return null;
        }
    }

}
