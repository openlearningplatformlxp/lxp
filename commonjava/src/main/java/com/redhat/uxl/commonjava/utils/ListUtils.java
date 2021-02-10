package com.redhat.uxl.commonjava.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * The type List utils.
 */
public final class ListUtils {
    private ListUtils() {
    }

    /**
     * Split list.
     *
     * @param <T>     the type parameter
     * @param list    the list
     * @param maxSize the max size
     * @return the list
     */
    public static <T> List<List<T>> split(List<T> list, final int maxSize) {
        List<List<T>> parts = new ArrayList<List<T>>();
        final int size = list.size();

        for (int i = 0; i < size; i += maxSize) {
            parts.add(new ArrayList<T>(list.subList(i, Math.min(size, i + maxSize))));
        }

        return parts;
    }
}
