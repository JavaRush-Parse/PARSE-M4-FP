package ua.com.javarush.parse.m4;

import ua.com.javarush.parse.m4.domain.City;
import ua.com.javarush.parse.m4.redis.CityCountry;
import ua.com.javarush.parse.m4.service.DataHandler;
import ua.com.javarush.parse.m4.util.HibernateUtil;
import ua.com.javarush.parse.m4.util.RedisUtil;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            DataHandler dataHandler = new DataHandler();

            List<City> allCities = dataHandler.fetchData();

            List<CityCountry> preparedData = dataHandler.transformData(allCities);

            dataHandler.pushToRedis(preparedData);

            List<Integer> ids = List.of(3, 2545, 123, 4, 189, 89, 3458, 1189, 10, 102);

            long startRedis = System.currentTimeMillis();
            List<CityCountry> citiesFromRedis = dataHandler.getCitiesFromRedis(ids);
            long stopRedis = System.currentTimeMillis();

            System.out.println("\n--- Data from Redis ---");
            citiesFromRedis.forEach(System.out::println);

            long startMysql = System.currentTimeMillis();
            dataHandler.testMysqlData(ids);
            long stopMysql = System.currentTimeMillis();

            System.out.println("\n--- Performance Results ---");
            System.out.printf("%s:\t%d ms\n", "Redis", (stopRedis - startRedis));
            System.out.printf("%s:\t%d ms\n", "MySQL", (stopMysql - startMysql));

        } catch (Exception e) {
            System.err.println("An application error occurred:");
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

    private static void shutdown() {
        System.out.println("\nShutting down resources...");
        HibernateUtil.shutdown();
        RedisUtil.getInstance().shutdown();
        System.out.println("Shutdown complete.");
    }
}
