package com.dm.collections;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Iterables {
    private Iterables() {
    }

    public static boolean isEmpty(Iterable<?> iterable) {
        return iterable == null || !iterable.iterator().hasNext();
    }

    public static boolean isNotEmpty(Iterable<?> iterable) {
        return !isEmpty(iterable);
    }

    public static <T> Stream<T> toStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T> Stream<T> toStream(Iterable<T> iterable, boolean parallel) {
        return StreamSupport.stream(iterable.spliterator(), parallel);
    }

    public static <I, R> List<R> mapToList(Iterable<I> iterable, Function<I, R> converter) {
        return StreamSupport.stream(iterable.spliterator(), false).map(converter).collect(Collectors.toList());
    }
}
