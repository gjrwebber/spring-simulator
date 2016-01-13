package org.springframework.simulator.sample;

import org.springframework.simulator.annotation.SimulateCall;
import org.springframework.simulator.annotation.SimulateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by gman on 7/01/16.
 */
@Service
public class MyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyService.class);

    @SimulateCall
    public void doSomething(int num, MyObject obj) {
        LOGGER.info(new Date() + ": Number " + num + " with " + obj);
    }

    @SimulateCall
    public void doSomething(int num) {
        LOGGER.info(new Date() + ": Number " + num);
    }

    @SimulateResult
    public String getSomething(MyObject obj) {
        return "" + obj;
    }
}
