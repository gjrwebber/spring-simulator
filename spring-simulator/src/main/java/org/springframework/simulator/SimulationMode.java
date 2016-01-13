package org.springframework.simulator;

import org.springframework.core.env.Environment;

/**
 * Created by gman on 8/01/16.
 */
public enum SimulationMode {

    RECORD,

    SIMULATE,

    SIMULATE_AND_EXIT,

    SIMULATE_AND_REPEAT,

    OFF;

    public boolean isSimulating() {return this == SimulationMode.SIMULATE || this == SimulationMode.SIMULATE_AND_EXIT || this == SimulationMode.SIMULATE_AND_REPEAT;}

    public boolean isRecording() {return this == SimulationMode.RECORD;}

    public static SimulationMode fromEnvironment(Environment environment) {
        SimulationMode simulationMode;
        if (environment.acceptsProfiles("simulate", "SIMULATE")) {
            simulationMode = SimulationMode.SIMULATE;
            if (environment.acceptsProfiles("exit", "EXIT")) {
                simulationMode = SimulationMode.SIMULATE_AND_EXIT;
            } else if (environment.acceptsProfiles("repeat", "REPEAT")) {
                simulationMode = SimulationMode.SIMULATE_AND_REPEAT;
            }
        } else if (environment.acceptsProfiles("simulate-and-exit", "SIMULATE_AND_EXIT")) {
            simulationMode = SimulationMode.SIMULATE_AND_EXIT;
        } else if (environment.acceptsProfiles("simulate-repeat", "SIMULATE_REPEAT")) {
            simulationMode = SimulationMode.SIMULATE_AND_REPEAT;
        } else if (environment.acceptsProfiles("record", "RECORD")) {
            simulationMode = SimulationMode.RECORD;
        } else {
            simulationMode = SimulationMode.OFF;
        }
        return simulationMode;
    }
}
