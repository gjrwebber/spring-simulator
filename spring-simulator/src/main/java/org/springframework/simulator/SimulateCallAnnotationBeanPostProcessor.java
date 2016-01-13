package org.springframework.simulator;

import org.gw.commons.aspects.TimeShiftAspect;
import org.gw.objectlogger.TimestampedObjectSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.simulator.annotation.SimulateCall;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Bean post-processor that automatically applies simulation behaviour
 * to any bean's method that is annotated with
 * {@link org.springframework.simulator.annotation.SimulateCall @SimulateCall} or
 * This is done by adding a corresponding {@link SimulateCallAnnotationAdvisor} to the
 * exposed proxy (either an existing AOP proxy or a newly generated proxy that
 * implements all of the target's interfaces).
 * <p>
 * The {@code postProcessAfterInitialization()} method of this post-processor checks {@code SimulationMode#isSimulating()} and
 * sets up the simulation of the methods calls that were
 * marked with {@link org.springframework.simulator.annotation.SimulateCall @SimulateCall}. It does this
 * by checking the bean for the
 * {@link org.springframework.simulator.annotation.SimulateCall @SimulateCall} annotation,
 * then looks up {@link RecordedMethod}s in the {@link RecordedMethodLogger} and
 * creates a {@link MethodCallSimulator} adding
 * it to the set. For each annotated bean it finds the earliest method call in the dataset. This will be used
 * by the {@link Simulator} to set the system time of the application.
 * <p>
 * This post-processor listens for {@link ContextRefreshedEvent} at which point it stops any existing {@link Simulator}
 * and starts a new one. It also listens for {@link ContextClosedEvent} at which time it stops any existing {@link Simulator}.
 *
 * @author Gman
 * @see org.springframework.simulator.annotation.SimulateCall
 * @see SimulateCallAnnotationAdvisor
 */
public class SimulateCallAnnotationBeanPostProcessor extends AbstractSimulateBeanPostProcessor implements ApplicationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulateCallAnnotationBeanPostProcessor.class);

    public SimulateCallAnnotationBeanPostProcessor(Environment environment) {
        super(environment);
    }

    @Autowired(required = false)
    private TimeShiftAspect timeShiftAspect;

    @Autowired
    private RecordedMethodLoggerSupport recordedMethodLoggerSupport;

    private Date earliestMethodInvocation = null;
    private Set<MethodCallSimulator> executors = new HashSet<>();
    private Simulator simulator;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        SimulateCallAnnotationAdvisor advisor = new SimulateCallAnnotationAdvisor(recordedMethodLoggerSupport, simulationMode);
        this.advisor = advisor;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (simulationMode.isSimulating()) {
            for (Method method : bean.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(SimulateCall.class)) {

                    LOGGER.info("Post processing " + method + " as @SimulateCall...");
                    RecordedMethodLogger logger = recordedMethodLoggerSupport.getLogger(recordedMethodLoggerSupport.getKey(method.getDeclaringClass(), method.getName(), method.getParameterTypes()));

                    try {
                        // Get the RecordedMethods
                        TimestampedObjectSet<RecordedMethod> records = logger.getDataSource().getAll(RecordedMethod.class);

                        // Get the earliest time to shift time
                        Date firstLogTime = records.asTimestampedList().get(0).getLogTime();
                        if (earliestMethodInvocation == null || firstLogTime.before(earliestMethodInvocation)) {
                            earliestMethodInvocation = firstLogTime;
                        }

                        // Create the MethodExecutor
                        MethodCallSimulator executor = new MethodCallSimulator(bean, method, records);
                        executors.add(executor);
                    } catch (FileNotFoundException e) {
                        LOGGER.warn("Attempted to simulate playback on " + method + ", but no RecordedMethod data logger file: " + e.getMessage());
                    }

                }
            }

        } else {
            LOGGER.info("simulate profile not set. Ignoring all @SimulateCall methods.");
        }

        bean = super.postProcessAfterInitialization(bean, beanName);

        return bean;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            stopSimulator();
            startSimulator();
        } else if (event instanceof ContextClosedEvent) {
            stopSimulator();
        }
    }

    private void startSimulator() {
        LOGGER.info("Starting simulator...");
        if (executors.size() > 0) {
            simulator = new Simulator(timeShiftAspect, earliestMethodInvocation, executors, simulationMode);
            Thread t = new Thread(simulator, "Simulation Thread");
            t.start();
        }
    }

    private void stopSimulator() {
        LOGGER.info("Shutting down simulator...");
        if (simulator != null) {
            simulator.shutdown();
        }
    }
}
