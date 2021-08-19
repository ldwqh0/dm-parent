package com.dm.collections;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public static <K> String getStringOrDefault(Map<K, Object> map, K key, String defaultValue) {
        Object value = map.getOrDefault(key, defaultValue);
        if (Objects.isNull(value)) {
            return null;
        } else if (value instanceof String) {
            return (String) value;
        } else {
            return String.valueOf(value);
        }
    }

    public static <K> String getString(Map<K, Object> map, K key) {
        return getStringOrDefault(map, key, null);
    }

    public static <K> Long getLong(Map<K, ? super Object> map, K key) {
        return getLongOrDefault(map, key, null);
    }

    public static <K> Long getLongOrDefault(Map<K, ? super Object> map, K key, Long defaultValue) {
        Object value = map.getOrDefault(key, defaultValue);
        if (Objects.isNull(value)) {
            return null;
        } else if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof String) {
            return Long.valueOf((String) value);
        } else {
            return Long.valueOf(String.valueOf(value));
        }
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
     * @param keyMapper 键提取器
     * @return 一个hashmap
     */
    public static <K, V> HashMap<K, V> hashMap(Iterable<V> iterable, Function<V, K> keyMapper) {
        HashMap<K, V> result = new HashMap<>();
        if (Iterables.isNotEmpty(iterable)) {
            iterable.forEach(item -> result.put(keyMapper.apply(item), item));
        }
        return result;
    }

    /**
     * 将一组可迭代的值转换为一个hashMap
     *
     * @param iterable    可迭代的值
     * @param keyMapper   获取key的方法
     * @param valueMapper 获取value的方法
     * @param <T>         值类型
     * @param <K>         Map键类型
     * @param <V>         Map值类型
     * @return 一个hashmap
     */
    public static <T, K, V> Map<K, V> hashMap(Iterable<T> iterable, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        HashMap<K, V> result = new HashMap<>();
        if (Iterables.isNotEmpty(iterable)) {
            iterable.forEach(item -> result.put(keyMapper.apply(item), valueMapper.apply(item)));
        }
        return result;
    }


    /**
     * 将一组可迭代的值转换为map,不同于hashMap方法,它使用Stream api,并且返回的类型不是hashmap
     *
     * @param iterable    可迭代的值
     * @param keyMapper   键转换器
     * @param valueMapper 值转换器
     * @param <T>         可迭代的值类型
     * @param <K>         结果数据的键类型
     * @param <V>         结果数据的值类型
     * @return 一个不可变更的map
     */
    public static <T, K, V> Map<K, V> map(Iterable<T> iterable, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return StreamSupport.stream(iterable.spliterator(), true)
            .collect(Collectors.toMap(keyMapper, valueMapper));
    }

    /**
     * 将一组可迭代的值转为为特定键的map对象,不同于hashmap,它使用Stream api,并且返回的类型不是hashmap
     *
     * @param iterable  可迭代的值
     * @param keyMapper 键转换器
     * @param <K>       结果数据的键类型
     * @param <V>       值类型
     * @return 一个不可变更的map
     */
    public static <K, V> Map<K, V> map(Iterable<V> iterable, Function<V, K> keyMapper) {
        return StreamSupport.stream(iterable.spliterator(), true)
            .collect(Collectors.toMap(keyMapper, Function.identity()));
    }

    public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> input, Function<? super V1, V2> valueConverter) {
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

    public static <K1, K2, V> Map<K2, V> transformKeys(Map<K1, V> input, Function<? super K1, K2> converter) {
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
