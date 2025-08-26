package ua.com.javarush.parse.m4.util;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ua.com.javarush.parse.m4.domain.City;
import ua.com.javarush.parse.m4.domain.Country;
import ua.com.javarush.parse.m4.domain.CountryLanguage;

public class HibernateUtil {
  @Getter
  private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
    Configuration configuration = new Configuration()
            .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect") // Змінено з MySQL8Dialect
            .setProperty("hibernate.connection.driver_class", "com.p6spy.engine.spy.P6SpyDriver")
            .setProperty("hibernate.connection.url", "jdbc:p6spy:mysql://localhost:3306/world")
            .setProperty("hibernate.connection.username", "root")
            .setProperty("hibernate.connection.password", "root")
            .setProperty("hibernate.current_session_context_class", "thread")
            .setProperty("hibernate.hbm2ddl.auto", "validate")
            .setProperty("hibernate.jdbc.batch_size", "100")
            .addAnnotatedClass(City.class)
            .addAnnotatedClass(Country.class)
            .addAnnotatedClass(CountryLanguage.class);
    return configuration.buildSessionFactory();
  }

  public static void shutdown() {
    if (sessionFactory != null) {
      sessionFactory.close();
    }
  }
}