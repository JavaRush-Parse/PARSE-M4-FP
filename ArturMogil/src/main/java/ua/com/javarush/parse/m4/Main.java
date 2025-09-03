package ua.com.javarush.parse.m4;

import ua.com.javarush.parse.m4.domain.City;
import ua.com.javarush.parse.m4.redis.CityCountry;
import ua.com.javarush.parse.m4.service.DataService;
import ua.com.javarush.parse.m4.util.HibernateUtil;
import ua.com.javarush.parse.m4.util.RedisUtil;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            DataService dataService = new DataService();

            List<City> cities = dataService.fetchData();

            List<CityCountry> transformData = dataService.transformData(cities);

            dataService.pushToRedis(transformData);

            List<Integer> ids = List.of(3, 2545, 123, 4, 189, 89, 3458, 1189, 10, 102);

            long startRedis = System.currentTimeMillis();
            List<CityCountry> citiesFromRedis = dataService.getCitiesFromRedis(ids);
            long stopRedis = System.currentTimeMillis();

            System.out.println("\n--- Data from Redis ---");
            citiesFromRedis.forEach(System.out::println);

            long startMysql = System.currentTimeMillis();
            dataService.testMysqlData(ids);
            long stopMysql = System.currentTimeMillis();

            System.out.println("\n--- Performance Results ---");
            System.out.printf("%s:\t%d ms\n", "Redis", (stopRedis - startRedis));
            System.out.printf("%s:\t%d ms\n", "MySQL", (stopMysql - startMysql));
        }catch (Exception e) {
            System.err.println("An application error occurred:");
            e.printStackTrace();
        } finally {
            shutdown();
        }

    }
    private static void shutdown() {
        HibernateUtil.shutdown();
        RedisUtil.getInstance().shutdown();
        System.out.println("Shutdown complete");
    }
}