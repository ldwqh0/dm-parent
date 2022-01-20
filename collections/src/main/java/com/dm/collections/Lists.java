package com.dm.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * List操作的工具类
 */
public final class Lists {
    private Lists() {
    }

    /**
     * 创建一个新的空的 {@link ArrayList}
     *
     * @param <E> 列表内容的类型
     * @return 一个新的arrayList
     */
    public static <E> ArrayList<E> arrayList() {
        return new ArrayList<>();
    }

    /**
     * 使用一系列元素创建一个新的 {@link ArrayList}
     *
     * @param elements 一系列的元素
     * @param <E>      集合元素类型
     * @return 一个包含指定元素的ArrayList
     */
    @SafeVarargs
    public static <E> ArrayList<E> arrayList(E... elements) {
        ArrayList<E> result = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(elements)) {
            Collections.addAll(result, elements);
        }
        return result;
    }

    public static <E> ArrayList<E> arrayList(Collection<E> collection) {
        ArrayList<E> result = arrayList();
        if (CollectionUtils.isNotEmpty(collection)) {
            result.addAll(collection);
        }
        return result;
    }


    /**
     * 通过指定的Iterable构建新的{@link ArrayList}<br>
     * 始终会返回一个{@link ArrayList}，无论给定的参数是否为空
     *
     * @param <E>      元素的类型
     * @param iterable 所有元素
     * @return 一个包含所有元素的ArrayList
     */
    public static <E> ArrayList<E> arrayList(Iterable<E> iterable) {
        ArrayList<E> result = new ArrayList<>();
        if (Iterables.isNotEmpty(iterable)) {
            iterable.forEach(result::add);
        }
        return result;
    }


    /**
     * 将一个列表变换为另外一个列表
     *
     * @param <I>       输入元素类型
     * @param <R>       输出元素类型
     * @param input     输入的元素
     * @param converter 从输入到输出的转换
     * @return 转换后的列表
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

    public static <T> T last(List<T> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return null;
        } else {
            return collection.get(collection.size() - 1);
        }
    }
}
