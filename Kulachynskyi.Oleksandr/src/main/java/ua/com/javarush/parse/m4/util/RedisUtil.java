package ua.com.javarush.parse.m4.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import ua.com.javarush.parse.m4.redis.CityCountry;

import java.util.List;

public class RedisUtil {

  private final RedisClient redisClient = prepareRedisClient();
  private final ObjectMapper mapper = new ObjectMapper();
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


  public void pushToRedis(List<CityCountry> data) {
    try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
      RedisStringCommands<String, String> sync = connection.sync();
      for (CityCountry cityCountry : data) {
        try {
          sync.set(String.valueOf(cityCountry.getId()), mapper.writeValueAsString(cityCountry));
        } catch (JsonProcessingException e) {
          e.printStackTrace();
        }
      }

    }
  }


  public void testRedisData(List<Integer> ids) {
    try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
      RedisStringCommands<String, String> sync = connection.sync();
      for (Integer id : ids) {
        String value = sync.get(String.valueOf(id));
        try {
          CityCountry cityCountry = mapper.readValue(value, CityCountry.class);
          System.out.println(cityCountry);
        } catch (JsonProcessingException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void shutdown() {
    if (client != null) {
      client.shutdown();
    }
  }
}
