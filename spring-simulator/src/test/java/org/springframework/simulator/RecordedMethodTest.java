package org.springframework.simulator;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gman on 11/01/16.
 */
public class RecordedMethodTest {

    private Object result = new Object();
    private RecordedMethod m = new RecordedMethod(result, "key", 5, 4, 3);

    @Test
    public void should_handle_null_args() throws Exception {
        RecordedMethod m = new RecordedMethod("key", null);
        Object[] args = m.getArgsAsObjects();
        assertNotNull(args);
        assertEquals(0, args.length);
    }

    @Test
    public void should_handle_null_result() throws Exception {
        RecordedMethod m = new RecordedMethod("key", null);
        assertNull(m.getResult());
    }

    @Test
    public void should_convert_constructor_result_to_RecordedObject() throws Exception {
        RecordedObject recordedResult = m.getResult();
        assertNotNull(recordedResult);
        assertEquals(result, recordedResult.getValue());
        assertEquals(Object.class, recordedResult.getType());
    }

    @Test
    public void should_convert_constructor_args_to_RecordedObjects() throws Exception {
        List<RecordedObject> recordedArgs = m.getArgs();
        assertNotNull(recordedArgs);
        assertEquals(3, recordedArgs.size());
        int[] argsAsInts = new int[3];
        for (int i = 0; i < argsAsInts.length; i++) {
            argsAsInts[i] = (int) recordedArgs.get(i).getValue();
        }
        assertArrayEquals(new int[]{ 5, 4, 3 }, argsAsInts);
    }

    @Test
    public void should_convert_RecordedObjects_to_Objects() throws Exception {
        Object[] args = m.getArgsAsObjects();
        assertArrayEquals(new Integer[]{ 5, 4, 3 }, args);
    }
}