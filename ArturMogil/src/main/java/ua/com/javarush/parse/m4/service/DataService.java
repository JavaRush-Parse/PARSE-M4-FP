package ua.com.javarush.parse.m4.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ua.com.javarush.parse.m4.repository.CityRepository;
import ua.com.javarush.parse.m4.repository.CountryRepository;
import ua.com.javarush.parse.m4.domain.City;
import ua.com.javarush.parse.m4.mapper.CityCountryMapper;
import ua.com.javarush.parse.m4.redis.CityCountry;
import ua.com.javarush.parse.m4.util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class DataService {
    private final SessionFactory sessionFactory;
    private final RedisService<CityCountry> cityCountryRedisService;
    private final CityRepository cityDAO;
    private final CountryRepository countryDAO;

    private final CityCountryMapper cityCountryMapper;

    public DataService() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
        this.cityDAO = new CityRepository(sessionFactory);
        this.countryDAO = new CountryRepository(sessionFactory);
        this.cityCountryRedisService = new RedisService<>(CityCountry.class);

        this.cityCountryMapper = new CityCountryMapper();
    }

    public List<City> fetchData() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            countryDAO.getAll();
            int totalCount = cityDAO.getTotalCount();
            List<City> allCities = new ArrayList<>(totalCount);
            int step = 500;
            for (int i = 0; i < totalCount; i += step) {
                allCities.addAll(cityDAO.getItems(i, step));
            }
            session.getTransaction().commit();
            return allCities;
        }
    }

    public List<CityCountry> transformData(List<City> cities) {
        return cityCountryMapper.toDto(cities);
    }

    public void pushToRedis(List<CityCountry> data) throws JsonProcessingException {
        cityCountryRedisService.save(data, CityCountry::getId);
    }

    public List<CityCountry> getCitiesFromRedis(List<Integer> ids) throws JsonProcessingException {
        return cityCountryRedisService.getByIds(ids);
    }

    public void testMysqlData(List<Integer> ids) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            for (Integer id : ids) {
                cityDAO.getById(id);
            }
            session.getTransaction().commit();
        }
    }
}
