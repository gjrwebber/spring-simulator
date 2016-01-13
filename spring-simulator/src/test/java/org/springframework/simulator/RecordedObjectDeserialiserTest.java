package org.springframework.simulator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by gman on 11/01/16.
 */
public class RecordedObjectDeserialiserTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void should_deserialise() throws Exception {
        TestA obja = new TestA();
        TestB objb = new TestB();
        objb.inc = 5;
        obja.b = objb;
        RecordedObject recordedObj = new RecordedObject(obja);
        String json = mapper.writeValueAsString(recordedObj);

        RecordedObject deserialisedObj = mapper.readValue(json, RecordedObject.class);
        assertEquals(recordedObj, deserialisedObj);
    }

}