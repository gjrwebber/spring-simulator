package org.springframework.simulator;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

/**
 * Created by gman on 11/01/16.
 */
public class MethodCallSimulatorWithArgsTest extends MethodCallSimulatorTest {

    @Override
    protected Method getMethod() throws Exception {
        return TestObject.class.getDeclaredMethod("inc", int.class);
    }

    @Override
    protected RecordedMethod getRecordedMethod1() {
        return new RecordedMethod("bla.inc(int)", new Object[]{ 5 });
    }

    @Override
    protected RecordedMethod getRecordedMethod2() {
        return new RecordedMethod("bla.inc(int)", new Object[]{ 4 });
    }

    public void should_run_all_methodCalls() throws Exception {
        simulator.run();
        assertEquals(9, testObject.incCount);
    }
}