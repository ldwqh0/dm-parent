package com.dm.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Lists {
  private Lists() {
  }

  public static <E> ArrayList<E> arrayList() {
    return new ArrayList<>();
  }

  public static <E> ArrayList<E> arrayList(@SuppressWarnings("unchecked") E... e) {
    if (e == null) {
      return arrayList();
    } else {
      return new ArrayList<>(Arrays.asList(e));
    }
  }

  /**
   * 通过指定的collection构建新的{@link ArrayList}<br>
   * 始终会返回一个{@link ArrayList}，无论给定的参数是否为空
   * 
   * @param <E>
   * @param collection
   * @return
   */
  public static <E> ArrayList<E> arrayList(Collection<E> collection) {
    if (CollectionUtils.isEmpty(collection)) {
      return new ArrayList<>();
    } else {
      return new ArrayList<>(collection);
    }
  }

  /**
   * 通过指定的Iterable构建新的{@link ArrayList}<br>
   * 始终会返回一个{@link ArrayList}，无论给定的参数是否为空
   * 
   * @param <E>
   * @param iterable
   * @return
   */
  public static <E> ArrayList<E> arrayList(Iterable<E> iterable) {
    ArrayList<E> result = new ArrayList<>();
    if (Iterables.isNotEmpty(iterable)) {
      iterable.forEach(result::add);
    }
    return result;
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
