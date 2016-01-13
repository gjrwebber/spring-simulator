package org.springframework.simulator;

import org.gw.commons.aspects.TimeShiftAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by gman on 8/01/16.
 */
public class Simulator implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Simulator.class);

    private TimeShiftAspect timeShiftAspect;

    private Date simulationStartTime;
    private Set<MethodCallSimulator> executors;

    private SimulationMode simulationMode;
    private boolean shutdown;

    public Simulator(TimeShiftAspect timeShiftAspect, Date simulationStartTime, Set<MethodCallSimulator> executors, SimulationMode simulationMode) {
        this.timeShiftAspect = timeShiftAspect;
        this.simulationStartTime = simulationStartTime;
        this.executors = executors;
        this.simulationMode = simulationMode;
    }

    @Override
    public void run() {

        try {

            switch (simulationMode) {
            case SIMULATE:
                LOGGER.info("Running simulation once.");
                runExecutors();
                break;
            case SIMULATE_AND_EXIT:
                LOGGER.info("Running simulation once then exiting.");
                runExecutors();
                System.exit(0);
                break;
            case SIMULATE_AND_REPEAT:
                LOGGER.info("Running simulation on repeat.");
                while (!shutdown) {
                    runExecutors();
                }
            default:
                throw new IllegalStateException("Cannot handle unknown SimulationMode: " + simulationMode);
            }


        } catch (Exception e) {
            LOGGER.error("Simulation failed and will stop: " + e.getMessage(), e);
        }

    }

    private void runExecutors() {

        CountDownLatch finishGate = new CountDownLatch(executors.size());

        // Shift the time to the earliest invocation - 2s
        if (simulationStartTime != null) {
            shiftTimeToStart(simulationStartTime);
        }

        // Execute the MethodExecutors
        for (MethodCallSimulator simulator : executors) {
            simulator.setFinishGate(finishGate);

            LOGGER.info("Starting playback simulation of " + simulator.getMethod());
            Thread thread = new Thread(simulator, simulator.toString());
            thread.start();
        }

        try {
            while (!finishGate.await(5, TimeUnit.SECONDS)) {
                LOGGER.info("Still waiting for " + finishGate.getCount() + " MethodExecutors to finish...");
            }
        } catch (InterruptedException e) {
            LOGGER.error("Simulator interrupted: " + e.getMessage());
        }
    }

    /**
     * Shifts the time to the first avl message time - 10s.
     */
    public void shiftTimeToStart(Date firstLogTime) {

        LOGGER.info("Simulator first msg time: " + firstLogTime);
        timeShiftAspect.setSystemTime(firstLogTime.getTime() - 2000);

        LOGGER.info("Simulated time: " + new Date());
    }

    public void setTimeShiftAspect(TimeShiftAspect timeShiftAspect) {
        this.timeShiftAspect = timeShiftAspect;
    }

    public void setSimulationStartTime(Date simulationStartTime) {
        this.simulationStartTime = simulationStartTime;
    }

    public void setExecutors(Set<MethodCallSimulator> executors) {
        this.executors = executors;
    }

    public void setSimulationMode(SimulationMode simulationMode) {
        this.simulationMode = simulationMode;
    }

    public void shutdown() {
        shutdown = true;
    }
}
