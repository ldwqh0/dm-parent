package com.dm.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Lists {

  public static <E> ArrayList<E> arrayList(@SuppressWarnings("unchecked") E... e) {
    return new ArrayList<>(Arrays.asList(e));
  }

  public static <E> ArrayList<E> arrayList(Collection<E> collection) {
    return new ArrayList<>(collection);
  }

  public static <E> List<E> asList(Collection<E> collection) {
    if (collection == null) {
      return null;
    } else if (collection instanceof List) {
      return (List<E>) collection;
    } else {
      return arrayList(collection);
    }
  }

  /**
   * 将一个列表变换为另外一个列表
   * 
   * @param <I>
   * @param <R>
   * @param input
   * @param converter
   * @return
   */
  public static <I, R> List<R> transform(List<I> input, Function<? super I, ? extends R> converter) {
    if (input == null) {
      return null;
    } else if (input.isEmpty()) {
      return Collections.emptyList();
    } else {
      return input.stream().map(converter).collect(Collectors.toList());
    }
  }

}
