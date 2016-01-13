package org.springframework.simulator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gman on 5/01/16.
 */
public class RecordedMethod {
    private String key;
    private List<RecordedObject> args = new ArrayList<>();
    private RecordedObject result;

    public RecordedMethod() {}

    public RecordedMethod(String key) {
        this(null, key, new Object[0]);
    }

    public RecordedMethod(String key, Object... args) {
        this(null, key, args);
    }

    public RecordedMethod(Object result, String key, Object... args) {
        this.key = key;
        if (result != null) {
            this.result = new RecordedObject(ClassUtils.resolvePrimitiveIfNecessary(result.getClass()), result);
        }
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                this.args.add(new RecordedObject(ClassUtils.resolvePrimitiveIfNecessary(arg.getClass()), arg));
            }
        }
    }

    public String getKey() {
        return key;
    }

    public List<RecordedObject> getArgs() {
        return args;
    }

    @JsonIgnore
    public Object[] getArgsAsObjects() {
        Object[] objectArgs = new Object[args.size()];
        for (int i = 0; i < objectArgs.length; i++) {
            objectArgs[i] = args.get(i).getValue();
        }
        return objectArgs;
    }

    public RecordedObject getResult() {
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RecordedMethod{");
        sb.append("key='").append(key).append('\'');
        sb.append(", args=").append(args);
        sb.append(", result=").append(result);
        sb.append('}');
        return sb.toString();
    }
}
