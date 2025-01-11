package com.parking.parkinglot.ejb;

import com.parking.parkinglot.common.UserDto;
import com.parking.parkinglot.entities.User;
import com.parking.parkinglot.entities.UserGroup;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
public class UsersBean {
    private static final Logger LOG = Logger.getLogger(UsersBean.class.getName());

    @PersistenceContext
    EntityManager entityManager;

    @Inject
    PasswordBean passwordBean;

    public List<UserDto> findAllUsers() {
        LOG.info("findAllUsers");
        try {
            TypedQuery<User> typeQuery = entityManager.createQuery("SELECT u FROM User u", User.class);
            List<User> users = typeQuery.getResultList();
            return copyUserToDto(users);
        } catch (Exception ex) {
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

    public void createUser(String username, String email, String password, Collection<String> groups) {
        LOG.info("createUser");
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordBean.convertToSha256(password));
        entityManager.persist(newUser);
        assignGroupsToUser(username, groups);
    }

    private void assignGroupsToUser(String username, Collection<String> groups) {
        LOG.info("Assigning groups to user: " + username);
        for (String group : groups) {
            try {
                UserGroup userGroup = new UserGroup();
                userGroup.setUsername(username);
                userGroup.setUserGroup(group);
                entityManager.persist(userGroup);
                LOG.info("Persisted group: " + group);
            } catch (Exception e) {
                LOG.warning("Failed to persist group: " + group + " for user: " + username);
            }
        }
    }


    public Collection<String> findUsernamesByUserIds(Collection<Long> userIds) {
        List<String> usernames =
                entityManager.createQuery("SELECT u.username FROM User u WHERE u.id in :userIds", String.class)
                        .setParameter("userIds", userIds)
                        .getResultList();
        return usernames;
    }

    public UserDto findById(Long userId) {
        LOG.info("findById");
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new EJBException("User not found");
        }
        return new UserDto(user.getId(), user.getUsername(), user.getEmail());
    }

    public List<String> getAllRoles() {
        LOG.info("getAllRoles");
        return List.of("READ_CARS", "WRITE_CARS", "READ_USERS", "WRITE_USERS" , "INVOICING");
    }

    public void updateUser(Long userId, String email, String password, Collection<String> groups) {
        LOG.info("updateUser");

        try {
            User user = entityManager.find(User.class, userId);
            if (user == null) {
                throw new EJBException("User not found");
            }

            user.setEmail(email);

            if (password != null && !password.trim().isEmpty()) {
                user.setPassword(passwordBean.convertToSha256(password));
            }

            entityManager.createQuery("DELETE FROM UserGroup ug WHERE ug.username = :username")
                    .setParameter("username", user.getUsername())
                    .executeUpdate();

            assignGroupsToUser(user.getUsername(), groups);

            entityManager.merge(user);
        } catch (Exception ex) {
            throw new EJBException("Error updating user", ex);
        }
    }


    public UserDto findByUsername(String username) {
        LOG.info("Finding user by username: " + username);
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return new UserDto(user.getId(), user.getUsername(), user.getEmail());
        } catch (Exception e) {
            LOG.warning("User not found for username: " + username);
            return null; // Or throw an exception, depending on your use case
        }
    }

    public List<UserGroup> findAllGroups() {
        LOG.info("Fetching all user groups");
        return entityManager.createQuery("SELECT ug FROM UserGroup ug", UserGroup.class)
                .getResultList();
    }

}
