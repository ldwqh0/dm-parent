package com.dm.util;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

class AssertImpl<T> implements Assert<T> {
    private final T value;

    private final Predicate<T> predicate;

    AssertImpl(T value, Predicate<T> predicate) {
        this.value = value;
        this.predicate = predicate;
    }


    @Override
    public boolean test() {
        return predicate.test(value);
    }

    @Override
    public void ifTrue(Consumer<T> consumer) {
        if (this.test()) {
            consumer.accept(value);
        }
    }

    @Override
    public void ifTrueOrElse(Consumer<T> consumer, Consumer<T> consumer2) {
        if (test()) {
            consumer.accept(value);
        } else {
            consumer2.accept(value);
        }

    }

    @Override
    public void orElse(Consumer<T> consumer) {
        if (!test()) {
            consumer.accept(value);
        }
    }

    @Override
    public Assert<T> and(Predicate<T> predicate) {
        return new AssertImpl<>(value, v -> test() && predicate.test(v));
    }

    @Override
    public Assert<T> and(Assert<T> tAssert) {
        return new AssertImpl<>(value, v -> test() && tAssert.test());
    }


    @Override
    public Assert<T> or(Predicate<T> predicate) {
        return new AssertImpl<>(value, v -> (test() || predicate.test(v)));
    }

    @Override
    public Assert<T> or(Assert<T> tAssert) {
        return new AssertImpl<>(value, v -> test() || tAssert.test());
    }

    @Override
    public <Ex extends Throwable> void orElseThrow(Supplier<Ex> exceptionSupplier) throws Ex {
        if (!test()) {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public <Ex extends Throwable> void ifTrueOrElseThrow(Consumer<T> consumer, Supplier<Ex> exceptionSupplier) throws Ex {
        if (test()) {
            consumer.accept(value);
        } else {
            throw exceptionSupplier.get();
        }
    }
}
