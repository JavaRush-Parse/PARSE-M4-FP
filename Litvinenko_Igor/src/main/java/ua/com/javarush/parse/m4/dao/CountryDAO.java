package ua.com.javarush.parse.m4.dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ua.com.javarush.parse.m4.entity.Country;

import java.util.List;


public class CountryDAO extends GenericDAO<Country, Integer>{

    public CountryDAO(SessionFactory sessionFactory) {
        super(sessionFactory, Country.class);

    }

    public List<Country> getAll() {
        try (Session session = getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Query<Country> query = session.createQuery(
                        "select distinct c from Country c join fetch c.languages",
                        Country.class
                );
                List<Country> result = query.getResultList();
                tx.commit();
                return result;
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }
}