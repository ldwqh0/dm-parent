package com.dm.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Sets {
    private Sets() {
    }

    @SuppressWarnings("unchecked")
    public static <E> HashSet<E> hashSet(E... e) {
        return new HashSet<>(Arrays.asList(e));
    }

    public static <E> HashSet<E> hasSet(Iterable<E> iterable) {
        HashSet<E> result = new HashSet<E>();
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
}
