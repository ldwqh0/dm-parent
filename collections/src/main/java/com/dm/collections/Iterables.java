package com.dm.collections;

public interface Iterables {

  public static boolean isEmpty(Iterable<?> iterable) {
    return iterable == null || !iterable.iterator().hasNext();
  }
}
