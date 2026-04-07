package com.personnel.system.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOperation {
    String operationType() default "";
    String operationDescription() default "";
    boolean logParams() default true;
}