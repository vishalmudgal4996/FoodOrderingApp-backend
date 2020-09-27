package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class StateDao {

    @PersistenceContext
    private EntityManager entityManager;

    public StateEntity getStateByUuid(final String stateUuid) {
        try {
            return entityManager.createNamedQuery("stateByUuid", StateEntity.class).setParameter("uuid", stateUuid)
                    .getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }

    public StateEntity getStateById(final Long stateId) {
        try {
            return entityManager.createNamedQuery("stateById", StateEntity.class).setParameter("id", stateId)
                    .getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }
}
