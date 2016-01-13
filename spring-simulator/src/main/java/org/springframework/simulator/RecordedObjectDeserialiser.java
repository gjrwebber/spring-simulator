package org.springframework.simulator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by gman on 8/01/16.
 */
public class RecordedObjectDeserialiser extends JsonDeserializer<RecordedObject> {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public RecordedObject deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);

        try {
            Class<?> argType = Class.forName(node.get("type").asText());
            Object value = mapper.treeToValue(node.get("value"), argType);
            return new RecordedObject(argType, value);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not deserialise RecordedArgument: " + e.getMessage(), e);
        }

    }
}
