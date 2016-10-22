package com.example.cache

import java.util.function.BiConsumer
import redis.clients.jedis.Jedis
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.io.ObjectInputStream
import java.io.ByteArrayInputStream

object RedisUtils {
    def get(cache: Jedis) = (key: Serializable) => {
        deserialize(cache.get(serialize(key).get))
    }: Option[Serializable]

    def put(cache: Jedis) = (key: Serializable, value: Serializable) => { 
        cache.set(serialize(key).get, serialize(value).get)
    }: Unit
    
    def serialize(a: Serializable) = {
        try {
            val bo = new ByteArrayOutputStream()
            val so = new ObjectOutputStream(bo)
            so.writeObject(a)
            so.flush()
            Some(bo.toString())
        } catch {
            case e: IOException => None   
        }
    }: Option[String]

    def deserialize(a: String) = {
        try {
            val b = a.getBytes() 
            val bi = new ByteArrayInputStream(b)
            val si = new ObjectInputStream(bi)
            Some(si.readObject().asInstanceOf[Serializable]);
        } catch {
            case e @ (_ : RuntimeException | _ : ClassNotFoundException) => None
        }
    }: Option[Serializable]
}
