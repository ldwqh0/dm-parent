package com.dm.collections;

import java.util.*;
import java.util.function.Function;

public final class CollectionUtils {
    private CollectionUtils() {
    }

    public static <I, O> List<O> transform(List<I> input, Function<I, O> converter) {
        return Lists.transform(input, converter);
    }

    public static <I, O> Set<O> transform(Set<I> input, Function<I, O> converter) {
        return Sets.transform(input, converter);
    }

    @SuppressWarnings("unchecked")
    public static <I, O, IC extends Collection<I>, OC extends Collection<O>> OC transform(IC input,
                                                                                          Function<I, O> converter) {
        if (input instanceof Set) {
            Set<I> si = (Set<I>) input;
            return (OC) Sets.transform(si, converter);
        } else if (input instanceof List) {
            List<I> li = (List<I>) input;
            return (OC) Lists.transform(li, converter);
        } else {
            List<I> li = Lists.arrayList(input);
            return (OC) Lists.transform(li, converter);
        }
    }

    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> c) {
        return !isEmpty(c);
    }

    public static Boolean containsAny(Collection<?> coll1, Collection<?> coll2) {
        if (coll1.size() < coll2.size()) {
            for (final Object aColl1 : coll1) {
                if (coll2.contains(aColl1)) {
                    return true;
                }
            }
        } else {
            for (final Object aColl2 : coll2) {
                if (coll1.contains(aColl2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean containsAll(final Collection<?> coll1, final Collection<?> coll2) {
        if (coll2.isEmpty()) {
            return true;
        }
        final Iterator<?> it = coll1.iterator();
        final Set<Object> elementsAlreadySeen = new HashSet<>();
        for (final Object nextElement : coll2) {
            if (elementsAlreadySeen.contains(nextElement)) {
                continue;
            }

            boolean foundCurrentElement = false;
            while (it.hasNext()) {
                final Object p = it.next();
                elementsAlreadySeen.add(p);
                if (Objects.equals(nextElement, p)) {
                    foundCurrentElement = true;
                    break;
                }
            }

            if (!foundCurrentElement) {
                return false;
            }
        }
        return true;
    }
}
