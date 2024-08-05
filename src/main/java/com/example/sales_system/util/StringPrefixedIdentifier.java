package com.example.sales_system.util;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IdGeneratorType(StringPrefixedIdentifierGenerator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StringPrefixedIdentifier {
    String name();

    String sequenceName() default "";

    String prefix() default "ID_";

    String formatNumber() default "%d";

    /*
    * Require: sequenceName exited in database
    * */
}
