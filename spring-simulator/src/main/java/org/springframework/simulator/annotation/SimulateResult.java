package org.springframework.simulator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Indicates that the result of the method call can be recorded and simulated.
 * <p>
 * In terms of target method signatures, any parameter types are supported.
 * However they all must implement {@code equals()} if the simulation is to
 * accurately represent the method call.
 * <p>
 * E.g. Given the method below:
 * <pre>
 *     {@code @SimulateResult}
 *      String toString(Date date);
 *     </pre>
 * If Date did not implement {@code equals()} we could not return the result for
 * that date instance and the simulation would not be accurate.
 * <p>
 * Furthermore, the return type cannot be null.
 *
 * @author Gman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.TYPE })
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

    /**
     * If the captured method does not have a logged result for the given arguments it will mock the result if {@code mockIfNotAvailable} is true (default).
     *
     * @return true if you want to mock the result if it has not been captured, false otherwise.
     */
    boolean mockIfNotAvailable() default true;

}
