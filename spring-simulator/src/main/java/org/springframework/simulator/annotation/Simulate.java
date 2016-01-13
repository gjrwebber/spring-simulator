package org.springframework.simulator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by gman on 1/01/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.TYPE })
public @interface Simulate {

    /**
     * If the captured method does not have a logged result for the given arguments it will mock the result if {@code mockIfNotAvailable} is true (default).
     *
     * @return true if you want to mock the result if it has not been captured, false otherwise.
     */
    boolean mockIfNotAvailable() default true;

}
