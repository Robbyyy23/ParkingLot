package com.parking.parkinglot.ejb;

import com.parking.parkinglot.common.UserDto;
import com.parking.parkinglot.entities.User;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
public class UserBean {
    private static final Logger LOG = Logger.getLogger(UserBean.class.getName());
    @PersistenceContext
    EntityManager entityManager;
    public List<UserDto> findAllUsers(){
        LOG.info("findAllUsers");
        try{
            TypedQuery<User> typeQuery = entityManager.createQuery("SELECT u FROM User u", User.class);
            List<User> users = typeQuery.getResultList();
            return copyUserToDto(users);
        }catch (Exception ex)
        {
            throw new EJBException(ex);
        }
    }
    public List<UserDto> copyUserToDto(List<User> users) {
        return users.stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail()))
                .collect(Collectors.toList());
    }

}