package ua.com.javarush.parse.m4.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.List;

public abstract class GenericDAO<T, K extends Serializable> {
    private final SessionFactory sessionFactory;
    private final Class<T> entityClass;

    public GenericDAO(SessionFactory sessionFactory, Class<T> entityClass) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
    }

    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public List<T> getItems(int offset, int limit) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery<T> cq = cb.createQuery(entityClass);
                Root<T> root = cq.from(entityClass);
                cq.select(root);

                Query<T> query = session.createQuery(cq);
                query.setFirstResult(offset);
                query.setMaxResults(limit);

                List<T> result = query.getResultList();
                tx.commit();
                return result;
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    public int getTotalCount() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery<Long> cq = cb.createQuery(Long.class);
                Root<T> root = cq.from(entityClass);
                cq.select(cb.count(root));

                int count = Math.toIntExact(session.createQuery(cq).getSingleResult());
                tx.commit();
                return count;
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    public T getById(K id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                T entity = session.find(entityClass, id);
                tx.commit();
                return entity;
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }
}