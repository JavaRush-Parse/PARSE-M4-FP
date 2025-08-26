package ua.com.javarush.parse.m4.dao;


import org.hibernate.SessionFactory;
import ua.com.javarush.parse.m4.entity.City;



public class CityDAO extends GenericDAO<City, Integer> {

    public CityDAO(SessionFactory sessionFactory) {
        super(sessionFactory, City.class);
    }
}