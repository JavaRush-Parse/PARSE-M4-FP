package ua.com.javarush.parse.m4.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ua.com.javarush.parse.m4.dao.CityDAO;
import ua.com.javarush.parse.m4.dao.CountryDAO;
import ua.com.javarush.parse.m4.domain.City;
import ua.com.javarush.parse.m4.domain.Country;
import ua.com.javarush.parse.m4.mapper.CityMapper;
import ua.com.javarush.parse.m4.redis.CityCountry;
import ua.com.javarush.parse.m4.util.HibernateUtil;
import ua.com.javarush.parse.m4.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;

public class DataProcessor {
    private final SessionFactory sessionFactory;
    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;

    public DataProcessor() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
        this.cityDAO = new CityDAO(sessionFactory);
        this.countryDAO = new CountryDAO(sessionFactory);
    }

    public List<City> retrieveCities() {
        try (Session session = sessionFactory.getCurrentSession()) {
            List<City> cities = new ArrayList<>();
            session.beginTransaction();

            int totalCities = cityDAO.getTotalCount();
            int batchSize = 500;
            for (int offset = 0; offset < totalCities; offset += batchSize) {
                cities.addAll(cityDAO.getItems(offset, batchSize));
            }

            session.getTransaction().commit();
            return cities;
        }
    }

    public List<CityCountry> convertCities(List<City> cities) {
        return cities.stream()
                .map(CityMapper::toCityCountry)
                .toList();
    }

    public void saveToRedis(List<CityCountry> data) {
        RedisUtil.getInstance().pushToRedis(data);
    }

    public List<CityCountry> fetchFromRedis(List<Integer> cityIds) {
        List<CityCountry> result = new ArrayList<>();
        RedisUtil.getInstance().testRedisData(cityIds);
        return result;
    }

    public void queryMysqlData(List<Integer> cityIds) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            for (Integer id : cityIds) {
                City city = cityDAO.getById(id);
                city.getCountry().getLanguages();
            }
            session.getTransaction().commit();
        }
    }
}