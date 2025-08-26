package ua.com.javarush.parse.m4.util;


import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ua.com.javarush.parse.m4.entity.City;
import ua.com.javarush.parse.m4.entity.Country;
import ua.com.javarush.parse.m4.entity.CountryLanguage;

public class HibernateUtil {

  @Getter
  private static final SessionFactory sessionFactory = buildSessionFactory();

  private static SessionFactory buildSessionFactory() {
    Configuration configuration = new Configuration().configure();

    configuration.addAnnotatedClass(City.class);
    configuration.addAnnotatedClass(Country.class);
    configuration.addAnnotatedClass(CountryLanguage.class);

    return configuration.buildSessionFactory();
  }

}
