package com.dm.collections;

import java.util.*;
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

    @SuppressWarnings("unchecked")
    public static <E> HashSet<E> hashSet(E... e) {
        return new HashSet<>(Arrays.asList(e));
    }

    public static <E> HashSet<E> hashSet(Collection<E> collection) {
        return new HashSet<>(collection);
    }

    public static <E> HashSet<E> hasSet(Iterable<E>... iterables) {
        HashSet<E> result = new HashSet<>();
        for (Iterable<E> iterable : iterables) {
            iterable.forEach(result::add);
        }
        return result;
    }

    public static <E> HashSet<E> hashSet(Collection<E>... collections) {
        HashSet<E> result = new HashSet<>();
        for (Collection<E> collection : collections) {
            result.addAll(collection);
        }
        return result;
    }

    @SafeVarargs
    public static <T> Set<T> hashSet(Collection<T> origin, T... e) {
        HashSet<T> result = new HashSet<>(origin);
        if (Objects.nonNull(e)) {
            Collections.addAll(result, e);
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
