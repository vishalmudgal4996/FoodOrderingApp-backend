package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public CustomerEntity customerSignUp(CustomerEntity customerEntity){
        entityManager.persist(customerEntity);
        return customerEntity;
    }

    public CustomerEntity customerByContactNumber(String contactNumber){
        try{
            return entityManager.createNamedQuery("customerByContactNumber",CustomerEntity.class)
                    .setParameter("contactNumber",contactNumber).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

}
