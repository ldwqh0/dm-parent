package com.dm.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
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

    static Assert<BigDecimal> gt(BigDecimal number1, BigDecimal number2) {
        return from(number1, v -> number2.compareTo(v) > 0);
    }

    static Assert<BigInteger> gt(BigInteger number1, BigInteger number2) {
        return from(number1, v -> number2.compareTo(v) > 0);
    }

    static Assert<Double> gt(double number1, double number2) {
        return from(number1, v -> number2 > v);
    }

    static Assert<Float> gt(float number1, float number2) {
        return from(number1, v -> number2 > v);
    }

    static Assert<Long> gt(long number1, long number2) {
        return from(number1, v -> number2 > v);
    }

    static Assert<Integer> gt(int number1, int number2) {
        return from(number1, v -> number2 > v);
    }

    static Assert<Short> gt(short number1, short number2) {
        return from(number1, v -> number2 > v);
    }

    static Assert<Byte> gt(byte number1, byte number2) {
        return from(number1, v -> number2 > v);
    }

    static Assert<BigDecimal> goe(BigDecimal number1, BigDecimal number2) {
        return from(number1, v -> number2.compareTo(v) >= 0);
    }

    static Assert<BigInteger> goe(BigInteger number1, BigInteger number2) {
        return from(number1, v -> number2.compareTo(v) >= 0);
    }

    static Assert<Double> goe(double number1, double number2) {
        return from(number1, v -> number2 >= v);
    }

    static Assert<Float> goe(float number1, float number2) {
        return from(number1, v -> number2 >= v);
    }

    static Assert<Long> goe(long number1, long number2) {
        return from(number1, v -> number2 >= v);
    }

    static Assert<Integer> goe(int number1, int number2) {
        return from(number1, v -> number2 >= v);
    }

    static Assert<Short> goe(short number1, short number2) {
        return from(number1, v -> number2 >= v);
    }

    static Assert<Byte> goe(byte number1, byte number2) {
        return from(number1, v -> number2 >= v);
    }

    static Assert<BigDecimal> lt(BigDecimal number1, BigDecimal number2) {
        return from(number1, v -> number2.compareTo(v) < 0);
    }

    static Assert<BigInteger> lt(BigInteger number1, BigInteger number2) {
        return from(number1, v -> number2.compareTo(v) < 0);
    }

    static Assert<Double> lt(double number1, double number2) {
        return from(number1, v -> number2 < v);
    }

    static Assert<Float> lt(float number1, float number2) {
        return from(number1, v -> number2 < v);
    }

    static Assert<Long> lt(long number1, long number2) {
        return from(number1, v -> number2 < v);
    }

    static Assert<Integer> lt(int number1, int number2) {
        return from(number1, v -> number2 < v);
    }

    static Assert<Short> lt(short number1, short number2) {
        return from(number1, v -> number2 < v);
    }

    static Assert<Byte> lt(byte number1, byte number2) {
        return from(number1, v -> number2 < v);
    }

    static Assert<BigDecimal> loe(BigDecimal number1, BigDecimal number2) {
        return from(number1, v -> number2.compareTo(v) <= 0);
    }

    static Assert<BigInteger> loe(BigInteger number1, BigInteger number2) {
        return from(number1, v -> number2.compareTo(v) <= 0);
    }

    static Assert<Double> loe(double number1, double number2) {
        return from(number1, v -> number2 <= v);
    }

    static Assert<Float> loe(float number1, float number2) {
        return from(number1, v -> number2 <= v);
    }

    static Assert<Long> loe(long number1, long number2) {
        return from(number1, v -> number2 <= v);
    }

    static Assert<Integer> loe(int number1, int number2) {
        return from(number1, v -> number2 <= v);
    }

    static Assert<Short> loe(short number1, short number2) {
        return from(number1, v -> number2 <= v);
    }

    static Assert<Byte> loe(byte number1, byte number2) {
        return from(number1, v -> number2 <= v);
    }

    static <T> Assert<T> from(final T value, final Predicate<T> predicate) {
        return new AssertImpl<>(value, predicate);
    }

    static <T> Assert<T> eq(T ob1, Object ob2) {
        return from(ob1, v -> Objects.equals(v, ob2));
    }

    static <T> Assert<T> ne(T obj1, Object obj2) {
        return from(obj1, v -> !Objects.equals(v, obj2));
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
