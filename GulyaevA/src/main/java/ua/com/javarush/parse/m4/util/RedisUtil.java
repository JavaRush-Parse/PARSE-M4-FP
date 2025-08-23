package ua.com.javarush.parse.m4.util;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Getter;

@Getter
public class RedisUtil {
    private final RedisClient client = prepareRedisClient();
    private static RedisUtil INSTANCE;

    public static RedisUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RedisUtil();
        }
        return INSTANCE;
    }

    private RedisUtil() {}

    private RedisClient prepareRedisClient() {
        RedisClient redisClient = RedisClient.create(RedisURI.create("localhost", 6379));
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            System.out.println("\nConnected to Redis\n");
        }
        return redisClient;
    }

    public void shutdown() {
        if (client != null) {
            client.shutdown();
        }
    }
}
