package com.example.cache

import java.util.function.BiConsumer

object CacheUtils {
    def get[K, V](cache: collection.mutable.Map[K, V]) = (key: K) => {
        cache.get(key)
    }: Option[V]

    def put[K, V](cache: collection.mutable.Map[K, V]) = (key: K, value: V) => { 
        cache.put(key, value)
    }: Unit

    def memoize[K, V](get: K => Option[V], put: (K, V) => Unit, f: K => V) = {
        (key: K) => {
            val b = get(key).getOrElse(f.apply(key));
            put.apply(key, b)
            b
        }: V   
    }
}
