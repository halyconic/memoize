package com.example.cache;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.junit.Test;

public class CacheUtilsTest {

    @Test
    public void emptyCacheGet() {
        Map<Integer, Integer> cache = new HashMap<>();
        assertFalse(CacheUtils.get(cache).apply(1).isPresent());
    }

    @Test
    public void fullCacheGet() {
        Map<Integer, Integer> cache = new HashMap<>();
        cache.put(1, 1);
        assertTrue(CacheUtils.get(cache).apply(1).isPresent());
    }
    
    @Test
    public void cacheMemoize() {
        Map<Integer, Integer> cache = new HashMap<>();
        Function<Integer, Optional<Integer>> get = CacheUtils.get(cache);
        BiConsumer<Integer, Optional<Integer>> put = CacheUtils.put(cache);
        
        AtomicBoolean hit = new AtomicBoolean();
        Function<Integer, Integer> f = (Integer a) -> {
            hit.set(true);
            return a;
        };
        Function<Integer, Integer> g = CacheUtils.memoize(get, put, f);
        
        hit.set(false);
        assertTrue(f.apply(1).equals(1));
        assertTrue(hit.get());
        
        hit.set(false);
        assertTrue(g.apply(1).equals(1));
        assertTrue(hit.get());

        hit.set(false);
        assertTrue(g.apply(1).equals(1));
        assertFalse(hit.get());
        
        hit.set(false);
        assertTrue(g.apply(2).equals(2));
        assertTrue(hit.get());
    }
}
