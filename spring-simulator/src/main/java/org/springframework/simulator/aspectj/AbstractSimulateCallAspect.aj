package org.springframework.simulator.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by gman on 7/01/16.
 */
public abstract aspect AbstractSimulateCallAspect extends AbstractSimulateAspect {

    @Override
    public Object replay(String key, MethodSignature methodSignature, ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }
}