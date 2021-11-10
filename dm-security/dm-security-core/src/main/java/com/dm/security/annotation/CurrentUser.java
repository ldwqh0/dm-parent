package com.dm.security.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * 这是 {@link AuthenticationPrincipal}的一个别名.
 *
 * @author ldwqh0@outlook.com
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {

    @AliasFor(annotation = AuthenticationPrincipal.class)
    String expression() default "";

}
