package com.example.cache;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.ehcache.Cache;

public class EhcacheUtils {
    // get does not distinguish "null" from empty
    public static <K, V> Function<K, Optional<V>> get(Cache<K, V> cache) {
        return key -> Optional.ofNullable(cache.get(key));
    }
    
    public static <K, V> BiConsumer<K, Optional<V>> put(Cache<K, V> cache) {
        return (key, value) -> {
            if (value.isPresent()) {
                cache.put(key, value.get());
            } else {
                cache.remove(key);
            }
        };
    }
}
