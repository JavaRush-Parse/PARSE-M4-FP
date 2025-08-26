package ua.com.javarush.parse.m4;


import org.hibernate.SessionFactory;
import ua.com.javarush.parse.m4.dao.CityDAO;
import ua.com.javarush.parse.m4.dao.CountryDAO;
import ua.com.javarush.parse.m4.entity.City;
import ua.com.javarush.parse.m4.redis.CityCountry;
import ua.com.javarush.parse.m4.util.HibernateUtil;
import ua.com.javarush.parse.m4.util.RedisUtil;

import java.util.*;


public class Main {
    private final SessionFactory sessionFactory;
    private static RedisUtil redisClient;
    private static DataService dataService;

    public Main() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
        CityDAO cityDAO = new CityDAO(sessionFactory);
        CountryDAO countryDAO = new CountryDAO(sessionFactory);
        redisClient = RedisUtil.getInstance();
        dataService = new DataService(sessionFactory, cityDAO, countryDAO);
    }

    public static void main(String[] args) {
        Main main = new Main();
        List<City> allCities = dataService.fetchData(main);
        List<CityCountry> preparedData = dataService.transformData(allCities);
        redisClient.pushToRedis(preparedData);

        main.sessionFactory.getCurrentSession().close();

        List<Integer> ids = List.of(3, 2545, 123, 4, 189, 89, 3458, 1189, 10, 102);

        long startRedis = System.currentTimeMillis();
        redisClient.testRedisData(ids);
        long stopRedis = System.currentTimeMillis();

        long startMysql = System.currentTimeMillis();
        dataService.testMysqlData(ids);
        long stopMysql = System.currentTimeMillis();

        System.out.printf("%s:\t%d ms\n", "Redis", (stopRedis - startRedis));
        System.out.printf("%s:\t%d ms\n", "MySQL", (stopMysql - startMysql));

        main.shutdown();
    }

    private void shutdown() {
        if (Objects.nonNull(sessionFactory)) {
            sessionFactory.close();
        }
        if (Objects.nonNull(redisClient)) {
            redisClient.shutdown();
        }
    }
}