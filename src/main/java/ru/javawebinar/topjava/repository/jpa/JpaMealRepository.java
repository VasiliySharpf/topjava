package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {

        meal.setUser(em.getReference(User.class, userId));

        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        } else {
            if (em.createQuery("UPDATE Meal m " +
                    "SET m.dateTime=:dateTime, m.calories=:calories, m.description=:description " +
                    "WHERE m.id=:id AND m.user.id=:userId")
                    .setParameter("dateTime", meal.getDateTime())
                    .setParameter("calories", meal.getCalories())
                    .setParameter("description", meal.getDescription())
                    .setParameter("id", meal.getId())
                    .setParameter("userId", userId)
                    .executeUpdate() != 0) {
                return meal;
            }
            return null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                    .setParameter(1, id)
                    .setParameter(2, userId)
                    .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Query query = em.createQuery("SELECT m FROM Meal m WHERE m.id=:id AND m.user.id=:userId");
        List<Meal> meals = query.setParameter("id", id).setParameter("userId", userId).getResultList();
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL, Meal.class)
                    .setParameter(1, em.getReference(User.class, userId))
                    .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createQuery(
                "SELECT m FROM Meal m WHERE m.dateTime >=: startDateTime " +
                        "AND m.dateTime <: endDateTime AND m.user.id=:userId " +
                        "ORDER BY m.dateTime DESC")
                .setParameter("startDateTime", startDateTime)
                .setParameter("endDateTime", endDateTime)
                .setParameter("userId", userId)
                .getResultList();

    }

}