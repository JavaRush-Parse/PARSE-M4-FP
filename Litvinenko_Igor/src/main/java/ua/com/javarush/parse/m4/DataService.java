package ua.com.javarush.parse.m4;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ua.com.javarush.parse.m4.dao.CountryDAO;
import ua.com.javarush.parse.m4.dao.GenericDAO;
import ua.com.javarush.parse.m4.entity.City;
import ua.com.javarush.parse.m4.entity.Country;
import ua.com.javarush.parse.m4.entity.CountryLanguage;
import ua.com.javarush.parse.m4.redis.CityCountry;
import ua.com.javarush.parse.m4.redis.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DataService {

    private final SessionFactory sessionFactory;
    private final GenericDAO<City, Integer> cityDAO;
    private final CountryDAO countryDAO;

    public DataService(SessionFactory sessionFactory, GenericDAO<City, Integer> cityDAO, CountryDAO countryDAO) {
        this.sessionFactory = sessionFactory;
        this.cityDAO = cityDAO;
        this.countryDAO = countryDAO;
    }

    protected List<CityCountry> transformData(List<City> cities) {
        return cities.stream().map(city -> {
            CityCountry res = new CityCountry();
            res.setId(city.getId());
            res.setName(city.getName());
            res.setPopulation(city.getPopulation());
            res.setDistrict(city.getDistrict());

            Country country = city.getCountry();
            res.setAlternativeCountryCode(country.getAlternativeCode());
            res.setContinent(country.getContinent());
            res.setCountryCode(country.getCode());
            res.setCountryName(country.getName());
            res.setCountryPopulation(country.getPopulation());
            res.setCountryRegion(country.getRegion());
            res.setCountrySurfaceArea(country.getSurfaceArea());

            Set<CountryLanguage> countryLanguages = country.getLanguages();
            Set<Language> languages = countryLanguages.stream().map(cl -> {
                Language language = new Language();
                language.setLanguage(cl.getLanguage());
                language.setIsOfficial(cl.getIsOfficial());
                language.setPercentage(cl.getPercentage());
                return language;
            }).collect(Collectors.toSet());
            res.setLanguages(languages);

            return res;
        }).collect(Collectors.toList());
    }

    protected void testMysqlData(List<Integer> ids) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            for (Integer id : ids) {
                City city = cityDAO.getById(id);
                Set<CountryLanguage> languages = city.getCountry().getLanguages();
            }
            session.getTransaction().commit();
        }
    }

    protected List<City> fetchData(Main main) {
        try (Session session = sessionFactory.getCurrentSession()) {
            List<City> allCities = new ArrayList<>();
            session.beginTransaction();
            List<Country> countries = countryDAO.getAll();

            int totalCount = cityDAO.getTotalCount();
            int step = 500;
            for (int i = 0; i < totalCount; i += step) {
                allCities.addAll(cityDAO.getItems(i, step));
            }
            session.getTransaction().commit();
            return allCities;
        }
    }
}
