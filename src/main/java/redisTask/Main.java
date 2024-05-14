package redisTask;


import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);
        RedisMap redisMap = new RedisMap(jedis);

        redisMap.put("key1", 1);
        redisMap.put("key2", 2);
        redisMap.put("key3", 3);

        System.out.println(redisMap.get("key1"));

        System.out.println(redisMap.keySet());

        /*Set<HostAndPort> jedisClusterNodes = Set.of(
                new HostAndPort("host", port),
                new HostAndPort("host", port),
                new HostAndPort("host", port)
        );
        JedisCluster jedisCluster = new JedisCluster(jedisClusterNodes);

        RedisMap redisMap = new RedisMap(jedisCluster);*/

        //redisMap.put("key1", 1);
        //redisMap.put("key2", 1);
        //redisMap.put("key3", 1);

        //System.out.println(redisMap.get("x"));

    }

}
