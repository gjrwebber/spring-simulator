package org.springframework.simulator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the result of the method call can be simulated.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.TYPE })
@Simulate
public @interface SimulateResult {

    /**
     * The algorithm used to return a captured result.
     */
    enum RETURN_ALGORITHM {
        FIRST,
        LAST,
        RANDOM
    }

    /**
     * @return algorithm used to return a captured result.
     */
    RETURN_ALGORITHM returnAlgorithm() default RETURN_ALGORITHM.LAST;

}
