package ua.com.javarush.parse.m4.util;


import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

  @Getter
  private static final SessionFactory sessionFactory = buildSessionFactory();

  private static SessionFactory buildSessionFactory() {
    Configuration configuration = new Configuration().configure();

//    configuration.addAnnotatedClass(Category.class);

    return configuration.buildSessionFactory();
  }

  public static void shutdown() {
    if (getSessionFactory() != null) {
      getSessionFactory().close();
    }
  }

}
