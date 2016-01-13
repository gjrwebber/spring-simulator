package org.springframework.simulator;

import org.gw.objectlogger.IDataSource;
import org.gw.objectlogger.ObjectLogger;

/**
 * Created by gman on 5/01/16.
 */
public class RecordedMethodLogger extends ObjectLogger<RecordedMethod> {
    public RecordedMethodLogger() {
        super();
    }

    public RecordedMethodLogger(IDataSource dataSource) {
        super(dataSource);
    }

    public RecordedMethodLogger(int capacity) {
        super(capacity);
    }

    public RecordedMethodLogger(int capacity, IDataSource dataSource) {
        super(capacity, dataSource);
    }
}
