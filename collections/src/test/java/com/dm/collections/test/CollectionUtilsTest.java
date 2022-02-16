package com.dm.collections.test;

import com.dm.collections.CollectionUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectionUtilsTest {

    @Test
    public void testContainsAll() {
        List<String> all = Arrays.asList("a", "b", "c");
        List<String> sub = Arrays.asList("a", "c");
        List<String> noAll = Arrays.asList("a", "d");
        assertTrue(CollectionUtils.containsAll(all, sub));
        assertFalse(CollectionUtils.containsAll(all, noAll));
        assertTrue(CollectionUtils.containsAny(all, sub));
        assertTrue(CollectionUtils.containsAny(all, noAll));
    }
}
