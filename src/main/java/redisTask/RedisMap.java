package redisTask;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;
import java.util.stream.Collectors;

public class RedisMap implements Map<String, Integer> {

    private Jedis jedis;
    private JedisCluster jedisCluster;

    public RedisMap(Jedis jedis) {
        this.jedis = jedis;
    }

    public RedisMap(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    public int size() {

        if (jedis != null) {
            return (int) jedis.dbSize();
        } else if (jedisCluster != null) {
            return (int) jedisCluster.dbSize();
        }

        return 0;
    }

    @Override
    public boolean isEmpty() {

        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {

        if (jedis != null) {
            return jedis.exists((String) key);
        } else if (jedisCluster != null) {
            return jedisCluster.exists((String) key);
        }

        return false;
    }

    @Override
    public boolean containsValue(Object value) {

        Set<String> keys;

        if (jedis != null) {
            keys = jedis.keys("*");
            for (String key : keys) {
                String v = jedis.get(key);
                if (value.equals(v)) {
                    return true;
                }
            }

        } else if (jedisCluster != null) {
            keys = jedisCluster.keys("*");
            for (String key : keys) {
                String v = jedisCluster.get(key);
                if (value.equals(v)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Integer get(Object key) {

        String value = jedis.get((String) key);

        if (jedis != null) {
            value = jedis.get((String) key);
        } else if (jedisCluster != null) {
            value = jedisCluster.get((String) key);
        }

        if (value == null) {
            return null;
        }

        return Integer.parseInt(value);
    }

    @Override
    public Integer put(String key, Integer value) {

        String v = null;

        if (jedis != null) {
            v = jedis.set(key, "" + value);
        } else if (jedisCluster != null) {
            v = jedisCluster.set(key, "" + value);
        }

        if (v != null && v.equals("OK")) {
            return value;
        }

        return null;
    }

    @Override
    public Integer remove(Object key) {

        if (jedis != null) {
            return (int) jedis.del((String) key);
        } else if (jedisCluster != null) {
            return (int) jedisCluster.del((String) key);
        }

        return 0;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Integer> m) {

        if (jedis != null) {
            for (Entry<? extends String, ? extends Integer> entry : m.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                jedis.set(key, String.valueOf(value));
            }
        } else if (jedisCluster != null) {
            for (Entry<? extends String, ? extends Integer> entry : m.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                jedisCluster.set(key, String.valueOf(value));
            }
        }
    }

    @Override
    public void clear() {

        if (jedis != null) {
            jedis.flushAll();
        } else if (jedisCluster != null) {
            jedisCluster.flushAll();
        }
    }

    @Override
    public Set<String> keySet() {

        if (jedis != null) {
            return jedis.keys("*");
        } else if (jedisCluster != null) {
            return jedisCluster.keys("*");
        }

        return Collections.emptySet();
    }

    @Override
    public Collection<Integer> values() {

        if (jedis != null) {
            Set<String> keys = jedis.keys("*");

            String[] keysArray = keys.toArray(new String[0]);

            return jedis.mget(keysArray).stream().map(Integer::valueOf).collect(Collectors.toList());

        } else if (jedisCluster != null) {
            Set<String> keys = jedisCluster.keys("*");

            String[] keysArray = keys.toArray(new String[0]);

            return jedisCluster.mget(keysArray).stream().map(Integer::valueOf).collect(Collectors.toList());
        }

        return Collections.EMPTY_LIST;
    }

    @Override
    public Set<Entry<String, Integer>> entrySet() {
        Set<Entry<String, Integer>> entrySet = new HashSet<>();
        Map<String, String> redisMap = jedisCluster.hgetAll("redis-map");
        for (Entry<String, String> entry : redisMap.entrySet()) {
            String key = entry.getKey();
            Integer value = Integer.valueOf(entry.getValue());
            entrySet.add(new RedisEntry<>(key, value));
        }
        return entrySet;
    }



    static class RedisEntry<String, Integer> implements Map.Entry<String, Integer> {
        private final String key;
        private Integer value;

        public RedisEntry(String key, Integer value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        public Integer setValue(Integer value) {
            Integer oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

}

