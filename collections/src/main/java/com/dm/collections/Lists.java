package com.dm.collections;

import java.util.*;
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
     * @param e   一系列的元素
     * @param <E> 集合元素类型
     * @return 一个包含指定元素的ArrayList
     */
    public static <E> ArrayList<E> arrayList(E... e) {
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
     * @param <E>        元素的类型
     * @param collection 包含元素的原始集合
     * @return 一个包含元素的ArrayList
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
     * 将一系列集合转换为{@link List}
     *
     * @param collection 一个集合
     * @param <E>        集合元素类型
     * @return 如果传入元素为空，返回一个空集合，如果传入元素本身就是一个List,直接返回。否则返回一个ArrayList
     */
    public static <E> List<E> toList(Collection<E> collection) {
        if (collection == null) {
            return Collections.emptyList();
        } else if (collection instanceof List) {
            return (List<E>) collection;
        } else {
            return arrayList(collection);
        }
    }

    /**
     * 将一系列元素添加到现有List中
     *
     * @param list     现有list
     * @param elements 要添加的一系列元素
     * @param <E>      元素类型
     */
    public static <E> void addAll(List<E> list, E... elements) {
        if (!Objects.isNull(elements)) {
            list.addAll(Arrays.asList(elements));
        }
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
}
