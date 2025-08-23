package ua.com.javarush.parse.m4.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.KeyValue;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import ua.com.javarush.parse.m4.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RedisService<T> {
    private final Class<T> clazz;
    private final ObjectMapper mapper;
    private final RedisUtil redisUtil;

    public RedisService(Class<T> clazz) {
        this.clazz = clazz;
        this.mapper = new ObjectMapper();
        this.redisUtil = RedisUtil.getInstance();
    }

    public <K> void save(List<T> data, Function<T, K> keyExtractor) throws JsonProcessingException {
        try (StatefulRedisConnection<String, String> connection = redisUtil.getClient().connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (T item : data) {
                String key = String.valueOf(keyExtractor.apply(item));
                String value = mapper.writeValueAsString(item);
                sync.set(key, value);
            }
        }
    }

    public List<T> getByIds(List<?> keys) throws JsonProcessingException {
        List<T> result = new ArrayList<>();
        if (keys == null || keys.isEmpty()) {
            return result;
        }
        String[] stringKeys = keys.stream()
                .map(String::valueOf)
                .toArray(String[]::new);
        try (StatefulRedisConnection<String, String> connection = redisUtil.getClient().connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            List<KeyValue<String, String>> values = sync.mget(stringKeys);
            for (KeyValue<String, String> kv : values) {
                if (kv.hasValue()) {
                    T deserializedObject = mapper.readValue(kv.getValue(), clazz);
                    result.add(deserializedObject);
                }
            }
        }
        return result;
    }
}
