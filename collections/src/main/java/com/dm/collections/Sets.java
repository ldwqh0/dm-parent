package com.dm.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Sets {

  @SuppressWarnings("unchecked")
  public static <E> HashSet<E> hashSet(E... e) {
    return new HashSet<>(Arrays.asList(e));
  }

  public static <E> HashSet<E> hasSet(Iterable<E> c) {
    HashSet<E> result = new HashSet<E>();
    c.forEach(result::add);
    return result;
  }

  public static <E> HashSet<E> hashSet(Collection<E> c) {
    return new HashSet<>(c);
  }

  public static <I, O> Set<O> transform(Set<I> input, Function<? super I, ? extends O> converter) {
    if (input == null) {
      return null;
    } else if (input.isEmpty()) {
      return Collections.emptySet();
    } else {
      return input.stream().map(converter).collect(Collectors.toSet());
    }
  }
}
