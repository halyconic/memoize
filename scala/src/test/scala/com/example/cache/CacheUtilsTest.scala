

package com.example.cache

import java.util.concurrent.atomic.AtomicBoolean

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@Test
class CacheUtilsTest {

    @Test
    def emptyCacheGet() = {
        val cache = scala.collection.mutable.Map.empty[Int, Int]
        assertTrue(CacheUtils.get(cache).apply(1).isEmpty)
    }

    @Test
    def fullCacheGet() = {
        val cache = scala.collection.mutable.Map.empty[Int, Int]
        cache.put(1, 1)
        assertFalse(CacheUtils.get(cache).apply(1).isEmpty)
    }
    
    @Test
    def cacheMemoize() = {
        val cache = scala.collection.mutable.Map.empty[Int, Int]
        val get = CacheUtils.get(cache);
        val put = CacheUtils.put(cache);
        
        val hit = new AtomicBoolean();
        val f = (a: Int) => {
            hit.set(true)
            a
        }
        val g = CacheUtils.memoize(get, put, f)
        
        hit.set(false)
        assertTrue(f.apply(1) == 1)
        assertTrue(hit.get);

        hit.set(false)
        assertTrue(g.apply(1) == 1)
        assertTrue(hit.get);
        
        hit.set(false)
        assertTrue(g.apply(1) == 1)
        assertFalse(hit.get);

        hit.set(false)
        assertTrue(g.apply(1) == 1)
        assertTrue(hit.get);
    }
}