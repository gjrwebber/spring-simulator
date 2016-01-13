package org.springframework.simulator;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by gman on 8/01/16.
 */
@JsonDeserialize(using = RecordedObjectDeserialiser.class)
public class RecordedObject {

    private Class<?> type;
    private Object value;

    public RecordedObject() {
    }

    public RecordedObject(Object value) {
        this(value.getClass(), value);
    }

    public RecordedObject(Class<?> type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Class<?> getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RecordedObject{");
        sb.append("type=").append(type);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        RecordedObject that = (RecordedObject) object;

        if (type != null ? !type.equals(that.type) : that.type != null) {
            return false;
        }
        return !(value != null ? !value.equals(that.value) : that.value != null);

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
