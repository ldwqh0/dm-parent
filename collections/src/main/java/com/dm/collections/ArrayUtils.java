package com.dm.collections;

import java.lang.reflect.Array;

public final class ArrayUtils {
    private ArrayUtils() {
    }

    public static boolean isEmpty(Object[] array) {
        return getLength(array) == 0;
    }

    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    public static int getLength(final Object array) {
        if (array == null) {
            return 0;
        }
        return Array.getLength(array);
    }
}
