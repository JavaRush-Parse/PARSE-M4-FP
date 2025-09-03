package ua.com.javarush.parse.m4.repository;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ua.com.javarush.parse.m4.domain.Country;

import java.util.List;

public class CountryRepository {
    private final SessionFactory sessionFactory;

    public CountryRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Country> getAll() {
        Query<Country> query = sessionFactory.getCurrentSession()
                .createQuery("select c from Country c join fetch c.languages", Country.class);
        return query.list();
    }
}
