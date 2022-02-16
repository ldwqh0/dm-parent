package com.dm.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Set操作的工具类
 */
public final class Sets {
    private Sets() {
    }

    public static <E> HashSet<E> hashSet() {
        return new HashSet<>();
    }

    @SafeVarargs
    public static <E> HashSet<E> hashSet(E... e) {
        return CollectionUtils.add(new HashSet<>(), e);
    }

    public static <T> HashSet<T> hashSet(Collection<T> origin) {
        HashSet<T> result = hashSet();
        if (CollectionUtils.isNotEmpty(origin)) {
            result.addAll(origin);
        }
        return result;
    }

    public static <E> HashSet<E> hasSet(Iterable<E> iterable) {
        HashSet<E> result = hashSet();
        if (Iterables.isNotEmpty(iterable)) {
            iterable.forEach(result::add);
        }
        return result;
    }

    public static <I, O> Set<O> transform(Set<I> fromSet, Function<? super I, ? extends O> converter) {
        if (fromSet == null) {
            return null;
        } else if (fromSet.isEmpty()) {
            return Collections.emptySet();
        } else {
            return fromSet.stream().map(converter).collect(Collectors.toSet());
        }
    }
}
