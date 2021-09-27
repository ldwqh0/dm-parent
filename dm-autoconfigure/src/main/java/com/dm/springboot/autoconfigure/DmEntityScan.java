package com.dm.springboot.autoconfigure;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DmEntityScannerRegistrar.class)
public @interface DmEntityScan {

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation
     * declarations e.g.: {@code @EntityScan("org.my.pkg")} instead of
     * {@code @EntityScan(basePackages="org.my.pkg")}.
     *
     * @return the base packages to scan
     */
    @AliasFor("basePackages")
    String[] value() default {};

    /**
     * Base packages to scan for entities. {@link #value()} is an alias for (and mutually
     * exclusive with) this attribute.
     * <p>
     * package names.
     *
     * @return the base packages to scan
     */
    @AliasFor("value")
    String[] basePackages() default {};
}
