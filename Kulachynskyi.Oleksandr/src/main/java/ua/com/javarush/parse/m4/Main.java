package ua.com.javarush.parse.m4;

import ua.com.javarush.parse.m4.domain.City;
import ua.com.javarush.parse.m4.redis.CityCountry;
import ua.com.javarush.parse.m4.service.DataProcessor;
import ua.com.javarush.parse.m4.util.HibernateUtil;
import ua.com.javarush.parse.m4.util.RedisUtil;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            DataProcessor processor = new DataProcessor();

            List<City> cities = processor.retrieveCities();

            List<CityCountry> transformedData = processor.convertCities(cities);

            processor.saveToRedis(transformedData);

            List<Integer> cityIds = List.of(3, 2545, 123, 4, 189, 89, 3458, 1189, 10, 102);

            long redisStartTime = System.currentTimeMillis();
            List<CityCountry> redisCities = processor.fetchFromRedis(cityIds);
            long redisEndTime = System.currentTimeMillis();

            System.out.println("\n--- Cities from Redis ---");
            redisCities.forEach(System.out::println);

            long mysqlStartTime = System.currentTimeMillis();
            processor.queryMysqlData(cityIds);
            long mysqlEndTime = System.currentTimeMillis();

            System.out.println("\n--- Performance Metrics ---");
            System.out.printf("%s:\t%d ms\n", "Redis", (redisEndTime - redisStartTime));
            System.out.printf("%s:\t%d ms\n", "MySQL", (mysqlEndTime - mysqlStartTime));

        } catch (Exception e) {
            System.err.println("Application error occurred:");
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    private static void closeResources() {
        System.out.println("\nClosing resources...");
        HibernateUtil.shutdown();
        RedisUtil.getInstance().shutdown();
        System.out.println("Resources closed.");
    }
}