package com.example.cache

import java.util.function.BiConsumer
import redis.clients.jedis.Jedis
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.io.ObjectInputStream
import java.io.ByteArrayInputStream
import org.ehcache.Cache

object EhcacheUtils {
    def get[K, V](cache: Cache[K, V]) = (key: K) => {
        Option(cache.get(key))
    }: Option[V]
    
    def put[K, V](cache: Cache[K, V]) = (key: K, value: V) => { 
        cache.put(key, value)
    }: Unit
}
