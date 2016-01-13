package org.springframework.simulator.annotation;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by gman on 7/01/16.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SimulationConfigurationSelector.class)
public @interface EnableSimulation {

    /**
     * Indicate whether subclass-based (CGLIB) proxies are to be created as opposed
     * to standard Java interface-based proxies. The default is {@code false}. <strong>
     * Applicable only if {@link #mode()} is set to {@link AdviceMode#PROXY}</strong>.
     * <p>
     * <p>Note that setting this attribute to {@code true} will affect <em>all</em>
     * Spring-managed beans requiring proxying, not just those marked with {@code @Async}.
     * For example, other beans marked with Spring's {@code @Transactional} annotation
     * will be upgraded to subclass proxying at the same time. This approach has no
     * negative impact in practice unless one is explicitly expecting one type of proxy
     * vs another, e.g. in tests.
     */
    boolean proxyTargetClass() default false;

    /**
     * Indicate how async advice should be applied. The default is
     * {@link AdviceMode#PROXY}.
     *
     * @see AdviceMode
     */
    AdviceMode mode() default AdviceMode.PROXY;

    /**
     * Indicate the order in which the
     * {@link org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor}
     * should be applied. The default is {@link Ordered#LOWEST_PRECEDENCE} in order to run
     * after all other post-processors, so that it can add an advisor to
     * existing proxies rather than double-proxy.
     */
    int order() default Ordered.LOWEST_PRECEDENCE;
}
