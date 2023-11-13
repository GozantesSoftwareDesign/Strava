package org.gozantes.strava.server.data.dao;

import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.server.data.domain.Mergeable;

import javax.el.MethodNotFoundException;
import javax.persistence.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class DAO<T> implements IDAO<T> {
    protected static EntityManagerFactory emf = Persistence.createEntityManagerFactory("Strava");

    private Class objClass;
    private Method pkGetter;

    protected DAO(Class objClass, Method pkGetter) {
        Objects.requireNonNull(pkGetter);

        if (Arrays.stream(objClass.getMethods()).anyMatch((Method m) -> m.equals(pkGetter)))
            throw new MethodNotFoundException(String.format("No method called %s in class %s.", pkGetter, objClass.getName()));

        this.pkGetter = pkGetter;
    }

    protected abstract DAO<T> instance();

    public void delete(T object) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.remove(em.find(this.instance().objClass, this.instance().pkGetter.invoke(object)));
            tx.commit();

            Logger.getLogger().info(String.format("%s deleted successfully.", object.toString()));
        } catch (Exception e) {
            Logger.getLogger().warning(String.format("Could not delete object %s: %s", object.toString(), e.getMessage()));
        } finally {
            if (tx != null && tx.isActive())
                tx.rollback();

            em.close();
        }
    }

    public void store(T object) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            if (object instanceof Mergeable) {
                if (this.instance().find((String) this.instance().pkGetter.invoke(object)) != null)
                    em.merge(object);
                else
                    em.persist(object);
            } else
                em.persist(object);

            tx.commit();

            Logger.getLogger().info(String.format("%s stored successfully.", object.toString()));
        } catch (Exception e) {
            Logger.getLogger().warning(String.format("Could not store object %s: %s", object.toString(), e.getMessage()));
        } finally {
            if (tx != null && tx.isActive())
                tx.rollback();

            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        List<T> objs = new ArrayList<T>();

        try {
            tx.begin();

            Query q = em.createQuery(String.format("SELECT b FROM %s b", this.instance().objClass.getName()));
            objs = (List<T>) q.getResultList();

            tx.commit();
        } catch (Exception e) {
            Logger.getLogger().severe(String.format("There was an error trying to retrieve all objects of class %s: %s", e.getMessage()));
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            em.close();
        }

        return objs;
    }

    @SuppressWarnings("unchecked")
    public T find(String param) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        T ret = null;

        try {
            tx.begin();

            ret = (T) em.find(this.instance().objClass, param);

            tx.commit();

            Logger.getLogger().info(ret == null ? String.format("%s not found.", this.instance().objClass.getName()) : String.format("%s found.", ret.toString()));
        } catch (Exception e) {
            Logger.getLogger().warning(String.format("There was an error trying to find %s %s: %s", this.instance().objClass.getName(), this.instance().pkGetter.getName().startsWith("get") ? ("by " + this.instance().pkGetter.getName().substring("get".length()).toLowerCase()) : ("using method" + this.instance().pkGetter.getName()), e.getMessage()));
        } finally {
            if (tx != null && tx.isActive())
                tx.rollback();

            em.close();
        }

        return ret;
    }
}
