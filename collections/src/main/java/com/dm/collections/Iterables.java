package com.dm.collections;

public final class Iterables {
    private Iterables() {
    }

    public static boolean isEmpty(Iterable<?> iterable) {
        return iterable == null || !iterable.iterator().hasNext();
    }

    public static boolean isNotEmpty(Iterable<?> iterable) {
        return !isEmpty(iterable);
    }
}
