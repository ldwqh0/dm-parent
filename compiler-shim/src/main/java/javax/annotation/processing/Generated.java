package javax.annotation.processing;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * 一个编译垫片包，它旨在解决编译时错误，不应该被程序使用
 */
@Documented
@Retention(SOURCE)
@Target({PACKAGE, TYPE, METHOD, CONSTRUCTOR, FIELD,
    LOCAL_VARIABLE, PARAMETER})
public @interface Generated {
    String[] value();

    String date() default "";

    String comments() default "";
}
