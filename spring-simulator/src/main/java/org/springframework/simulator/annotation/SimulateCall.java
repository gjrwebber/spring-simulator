package org.springframework.simulator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a call to the method can be recorded and simulated.
 * <p>
 * In terms of target method signatures, any parameter types are supported.
 * However, the return type is constrained to {@code void}.
 *
 * @author Gman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
public @interface SimulateCall {

}
