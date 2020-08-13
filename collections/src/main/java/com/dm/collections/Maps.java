package com.dm.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class Maps {
  private Maps() {
  }

  public static boolean isEmpty(Map<?, ?> map) {
    return map == null || map.isEmpty();
  }

  public static boolean isNotEmpty(Map<?, ?> map) {
    return !isEmpty(map);
  }

  public static <K, V> HashMap<K, V> hashMap() {
    return new HashMap<>();
  }

  /**
   * 将一系列的值,放入hashMap中<br>
   * 使用func通过iterable获取key
   * 
   * @param <K>       键的类型
   * @param <V>       值的类型
   * @param iterable  值迭代器
   * @param converter 值提取器
   * @return
   */
  public static <K, V> HashMap<K, V> hashMap(Function<V, K> converter, Iterable<V> iterable) {
    HashMap<K, V> result = new HashMap<>();
    if (Iterables.isNotEmpty(iterable)) {
      iterable.forEach(item -> result.put(converter.apply(item), item));
    }
    return result;
  }

  /**
   * 通过一系列的键，生成map
   * 
   * @param <K>
   * @param <V>
   * @param iterable  一系列的键
   * @param converter 通过键获取值的方法
   * @return
   */
  public static <K, V> HashMap<K, V> hashMap(Iterable<K> iterable, Function<? super K, V> converter) {
    HashMap<K, V> result = new HashMap<>();
    if (Iterables.isNotEmpty(iterable)) {
      iterable.forEach(item -> result.put(item, converter.apply(item)));
    }
    return result;
  }
}
