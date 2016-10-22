package com.example.cache;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import redis.clients.jedis.Jedis;

public class RedisUtils {
    // get does not distinguish "null" from empty
    public static <K extends Serializable> Function<K, Optional<Serializable>> get(Jedis cache) {
        return key -> Optional.ofNullable(cache.get(serialize(key))).map(RedisUtils::deserialize);
    }
    
    public static <K extends Serializable, V extends Serializable> BiConsumer<K, Optional<V>> put(Jedis cache) {
        return (key, value) -> {
            if (value.isPresent()) {
                cache.set(serialize(key), serialize(value.get()));
            } else {
                cache.del(serialize(key));
            }
        };
    }
    
    private static String serialize(Serializable a) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(a);
            so.flush();
            return bo.toString();
        } catch (IOException e) {
            return null; // TODO better error handling
        }
    }
    
    private static Serializable deserialize(String a) {
        try {
            byte b[] = a.getBytes(); 
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si;
            si = new ObjectInputStream(bi);
            return (Serializable) si.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null; // TODO better error handling
        }
    }
}
