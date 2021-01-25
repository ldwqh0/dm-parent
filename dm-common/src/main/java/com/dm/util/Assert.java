package com.dm.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Assert<T> {

    boolean test();

    void ifTrue(Consumer<T> consumer);

    /**
     * 使用断言测试对值进行断言,并根据断言结果进行不同的消费处理
     *
     * @param consumer  断言成功的消费处理
     * @param consumer2 断言失败的消费处理
     */
    void ifTrueOrElse(Consumer<T> consumer, Consumer<T> consumer2);

    void orElse(Consumer<T> consumer);

    Assert<T> and(Predicate<T> predicate);

    Assert<T> and(Assert<T> tAssert);

    Assert<T> or(Predicate<T> predicate);

    Assert<T> or(Assert<T> tAssert);

    <Ex extends Throwable> void orElseThrow(Supplier<Ex> exceptionSupplier) throws Ex;

    <Ex extends Throwable> void ifTrueOrElseThrow(Consumer<T> consumer, Supplier<Ex> exceptionSupplier) throws Ex;

    static <T> Assert<T> from(final T value, final Predicate<T> predicate) {
        return new AssertImpl<>(value, predicate);
    }

    static <T> Assert<T> eq(T ob1, Object ob2) {
        return from(ob1, v -> Objects.equals(v, ob2));
    }

    static Assert<Boolean> always(boolean result) {
        return from(result, v -> result);
    }

    static <T> Assert<T> isNull(T v) {
        return from(v, Objects::isNull);
    }

    static <T> Assert<T> notNull(T t) {
        return from(t, Objects::nonNull);
    }

    /**
     * Checks if a CharSequence is not empty ("") and not null.
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty("")        = false
     * StringUtils.isNotEmpty(" ")       = true
     * StringUtils.isNotEmpty("bob")     = true
     * StringUtils.isNotEmpty("  bob  ") = true
     *
     * @param value the CharSequence to check, may be null
     * @return the {@link Assert}
     */
    static Assert<String> notEmpty(String value) {
        return from(value, StringUtils::isNotEmpty);
    }

    static <T extends Map<?, ?>> Assert<T> notEmpty(T map) {
        return from(map, v -> v != null && !v.isEmpty());
    }

    static <T extends Collection<?>> Assert<T> notEmpty(T collection) {
        return from(collection, v -> v != null && !v.isEmpty());
    }

}
