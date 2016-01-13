package org.springframework.simulator;

import org.gw.objectlogger.TimestampedObject;
import org.gw.objectlogger.TimestampedObjectSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gman on 11/01/16.
 */
public class MethodCallSimulatorTest {

    protected TestObject testObject = new TestObject();

    protected TimestampedObjectSet<RecordedMethod> methodCalls;

    protected MethodCallSimulator simulator;

    private TimestampedObject<RecordedMethod> m1;
    private TimestampedObject<RecordedMethod> m2;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        m1 = new TimestampedObject<>(getRecordedMethod1());
        m1.setLogTime(new Date(System.currentTimeMillis() + 100));
        m2 = new TimestampedObject<>(getRecordedMethod2());
        m2.setLogTime(new Date(System.currentTimeMillis() + 150));

        methodCalls = new TimestampedObjectSet<>();
        methodCalls.add(m1);
        methodCalls.add(m2);
        simulator = new MethodCallSimulator(testObject, getMethod(), methodCalls);

    }

    protected Method getMethod() throws Exception {
        return TestObject.class.getDeclaredMethod("call");
    }

    protected RecordedMethod getRecordedMethod1() {
        return new RecordedMethod("bla.call()");
    }

    protected RecordedMethod getRecordedMethod2() {
        return new RecordedMethod("bla.call()");
    }

    @Test
    public void should_run_all_methodCalls() throws Exception {
        simulator.run();
        assertEquals(2, testObject.callCount);
    }

    @Test
    public void should_run_all_methodCalls_at_correct_time() throws Exception {
        simulator.run();

        assertEquals(2, testObject.callDates.size());
        long m1CallTimeDiff = Math.abs(m1.getLogTime().getTime() - testObject.callDates.get(0).getTime());
        long m2CallTimeDiff = Math.abs(m2.getLogTime().getTime() - testObject.callDates.get(1).getTime());
        assertTrue("The time difference between when m1 should have been called and when it was called was " + m1CallTimeDiff + "ms which is > the acceptable 5ms", m1CallTimeDiff <= 5);
        assertTrue("The time difference between when m2 should have been called and when it was called was " + m2CallTimeDiff + "ms which is > the acceptable 5ms", m2CallTimeDiff <= 5);
    }

    public class TestObject {
        int callCount = 0;
        int incCount = 0;
        List<Date> callDates = new ArrayList<>();

        public void call() {
            callCount++;
            callDates.add(new Date());
        }

        public void inc(int inc) {
            incCount += inc;
            callDates.add(new Date());
        }
    }
}