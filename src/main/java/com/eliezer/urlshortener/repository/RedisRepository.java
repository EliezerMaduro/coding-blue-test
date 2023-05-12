package com.eliezer.urlshortener.repository;

import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

@Repository
public class RedisRepository {
    public void saveUrl(String hash, String url){
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.set(hash, url);
        jedis.close();
    }

    public String getUrl(String hash){
        Jedis jedis = new Jedis("localhost", 6379);
        String url = jedis.get(hash);
        jedis.close();
        return url;
    }
}
