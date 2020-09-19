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

    @SuppressWarnings("unchecked")
    public static <E> HashSet<E> hashSet(E... e) {
        return new HashSet<>(Arrays.asList(e));
    }

    public static <E> HashSet<E> hasSet(Iterable<E> iterable) {
        HashSet<E> result = new HashSet<>();
        if (Iterables.isNotEmpty(iterable)) {
            iterable.forEach(result::add);
        }
        return result;
    }

    public static <E> HashSet<E> hashSet(Collection<E> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return new HashSet<>();
        } else {
            return new HashSet<>(collection);
        }
    }

    public static <I, O> Set<O> transform(Set<I> fromeSet, Function<? super I, ? extends O> converter) {
        if (fromeSet == null) {
            return null;
        } else if (fromeSet.isEmpty()) {
            return Collections.emptySet();
        } else {
            return fromeSet.stream().map(converter).collect(Collectors.toSet());
        }
    }

    public static <T> Set<T> merge(Set<T> origin, T... e) {
        HashSet<T> result = hashSet(origin);
        if (e != null) {
            result.addAll(Arrays.asList(e));
        }
        return result;
    }

    public static <T> Set<T> merge(Collection<T> origin, Collection<T> elements) {
        HashSet<T> hashSet = hashSet(origin);
        if (CollectionUtils.isNotEmpty(elements)) {
            hashSet.addAll(elements);
        }
        return hashSet;
    }

}
