package com.dm.collections;

import java.util.Map;

public interface Maps {
  public static boolean isEmpty(Map<?, ?> map) {
    return map == null || map.isEmpty();
  }

  public static boolean isNotEmpty(Map<?, ?> map) {
    return !isEmpty(map);
  }
}
