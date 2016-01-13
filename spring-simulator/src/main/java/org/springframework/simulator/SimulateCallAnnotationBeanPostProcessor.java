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
 * Created by gman on 7/01/16.
 */
public class SimulateCallAnnotationBeanPostProcessor extends AbstractSimulateBeanPostProcessor implements ApplicationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulateCallAnnotationBeanPostProcessor.class);

    public SimulateCallAnnotationBeanPostProcessor(Environment environment) {
        super(environment);
    }

    @Autowired(required = false)
    private TimeShiftAspect timeShiftAspect;

    @Autowired
    private LoggerService loggerService;

    private Date earliestMethodInvocation = null;
    private Set<MethodCallSimulator> executors = new HashSet<>();

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        SimulateCallAnnotationAdvisor advisor = new SimulateCallAnnotationAdvisor(loggerService, simulationMode);
        this.advisor = advisor;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (simulationMode.isSimulating()) {
            for (Method method : bean.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(SimulateCall.class)) {

                    LOGGER.info("Post processing " + method + " as @SimulateCall...");
                    RecordedMethodLogger logger = loggerService.getLogger(loggerService.getKey(method.getDeclaringClass(), method.getName(), method.getParameterTypes()));

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

    private Simulator simulator;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            LOGGER.info("Starting simulator...");
            if (executors.size() > 0) {
                simulator = new Simulator(timeShiftAspect, earliestMethodInvocation, executors, simulationMode);
                Thread t = new Thread(simulator, "Simulation Thread");
                t.start();
            }
        } else if (event instanceof ContextClosedEvent) {
            LOGGER.info("Shutting down simulator...");
            if (simulator != null) {
                simulator.shutdown();
            }
        }
    }
}
