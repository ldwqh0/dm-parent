package com.dm.collections;

import java.util.Collections;
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

    @SafeVarargs
    public static <K, V> HashMap<K, V> hashMap(Map<K, V>... maps) {
        HashMap<K, V> result = hashMap();
        for (Map<K, V> map : maps) {
            if (isNotEmpty(map)) {
                result.putAll(map);
            }
        }
        return result;
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

    public static <K, V1, V2> Map<K, V2> transfromValues(Map<K, V1> input, Function<? super V1, V2> valueConverter) {
        if (input == null) {
            return null;
        }
        if (input.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<K, V2> result = new HashMap<>();
        input.forEach((key, value) -> result.put(key, valueConverter.apply(value)));
        return result;
    }

    public static <K1, K2, V> Map<K2, V> transformKeys(Map<K1, V> input,
                                                       Function<? super K1, K2> converter) {
        if (input == null) {
            return null;
        }
        if (input.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<K2, V> result = new HashMap<>();
        input.forEach((k, v) -> result.put(converter.apply(k), v));
        return result;
    }

    public static <K, V> HashMapBuilder<K, V> entry(K key, V value) {
        return new HashMapBuilder<>(key, value);
    }

    public final static class HashMapBuilder<K, V> {
        private final HashMap<K, V> map = new HashMap<>();

        public HashMapBuilder() {
        }

        public HashMapBuilder(K key, V value) {
            this.map.put(key, value);
        }

        public HashMapBuilder<K, V> entry(K key, V value) {
            map.put(key, value);
            return this;
        }

        public HashMap<K, V> build() {
            return map;
        }
    }
}
