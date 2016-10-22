package com.example.cache;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CacheUtils {
    // get does not distinguish "null" from empty
    public static <K, V> Function<K, Optional<V>> get(Map<K, V> cache) {
        return key -> Optional.ofNullable(cache.get(key));
    }
    
    public static <K, V> BiConsumer<K, Optional<V>> put(Map<K, V> cache) {
        return (key, value) -> {
            if (value.isPresent()) {
                cache.put(key, value.get());
            } else {
                cache.remove(key);
            }
        };
    }
    
    public static <K, V> Function<K, Optional<V>> getWithEmpty(Map<K, Optional<V>> cache) {
        return key -> {
            Optional<V> value = cache.get(key);
            if (value == null)
                return Optional.empty();
            else
                return value;
        };
    }
    
    public static <K, V> BiConsumer<K, Optional<V>> putWithEmpty(Map<K, Optional<V>> cache) {
        return (key, value) -> cache.put(key, value);
    }
    
    public static <K, V> Function<K, V> memoize(Function<K, Optional<V>> get, BiConsumer<K, Optional<V>> put, Function<K, V> f) {
        return key -> {
            Optional<V> a = get.apply(key);
            
            // orElse evaluates eagerly, so write the code lazily instead
            V value;
            if (a.isPresent()) {
                value = a.get();
            } else {
                value = f.apply(key);
            }
            
            put.accept(key, Optional.of(value));
            
            return value;
        };
    }
}
