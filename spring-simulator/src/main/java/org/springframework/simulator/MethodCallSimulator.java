package org.springframework.simulator;

import org.gw.objectlogger.TimestampedObject;
import org.gw.objectlogger.TimestampedObjectSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by gman on 6/01/16.
 */
public class MethodCallSimulator implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodCallSimulator.class);

    private Object instance;
    private Method method;
    private TimestampedObjectSet<RecordedMethod> methodCalls;
    private CountDownLatch finishGate;

    public MethodCallSimulator(Object instance, Method method, TimestampedObjectSet<RecordedMethod> methodCalls) {
        this.instance = instance;
        this.method = method;
        this.methodCalls = methodCalls;
    }

    @Override
    public void run() {

        try {

            List<TimestampedObject<RecordedMethod>> methodCallMap = methodCalls.asTimestampedList();
            // Get the List of messages for the next time key
            for (TimestampedObject<RecordedMethod> timestampedObject : methodCallMap) {

                /* Block this Thread until the time of the timestamped object*/
                blockUntilTime(timestampedObject.getLogTime());

                // send each message in the list
                RecordedMethod recordedMethod = timestampedObject.getObj();
                try {
                    method.invoke(instance, recordedMethod.getArgsAsObjects());
                } catch (Exception e) {
                    LOGGER.error("Could not execute " + recordedMethod + ": " + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            LOGGER.error("MethodExecutor for " + method + " failed and will stop: " + e.getMessage(), e);
        } finally {
            if (finishGate != null) {
                finishGate.countDown();
            }
        }

    }

    private void blockUntilTime(Date date) {
        LOGGER.info("Blocking until " + date);
        long time = date.getTime();
        long diff = time - System.currentTimeMillis();

        while (diff > 0L) {
            LOGGER.info("Need to block for " + diff + "ms");
            if (diff > 2L) {
                try {
                    LOGGER.info("Sleeping for " + diff + "ms");
                    Thread.sleep(diff - 2L);
                } catch (InterruptedException var5) {
                    var5.printStackTrace();
                }
            }
            diff = time - System.currentTimeMillis();
        }
    }

    public Method getMethod() {
        return method;
    }

    public TimestampedObjectSet<RecordedMethod> getMethodCalls() {
        return methodCalls;
    }

    public void setFinishGate(CountDownLatch finishGate) {
        this.finishGate = finishGate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MethodCallSimulator{");
        sb.append("instance=").append(instance);
        sb.append(", method=").append(method);
        sb.append('}');
        return sb.toString();
    }


}
