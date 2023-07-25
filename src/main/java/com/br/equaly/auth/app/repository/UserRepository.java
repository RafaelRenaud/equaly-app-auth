package com.br.equaly.auth.app.repository;

import com.br.equaly.auth.app.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByLogin(String login);

    @Query(
            "SELECT user FROM User user WHERE user.login = :login AND user.email= :email"
    )
    User findByLoginAndEmail(String login, String email);

    @Modifying(clearAutomatically = true)
    @Query(
           "UPDATE User user SET user.lastLogin = CURRENT_TIMESTAMP WHERE user.id = :id"
    )
    void updateLastLogin(Long id);

    @Modifying(clearAutomatically = true)
    @Query(
            "UPDATE User user SET user.password = :password, user.updatedAt = CURRENT_TIMESTAMP WHERE user.login = :login"
    )
    void updatePassword(String login, String password);
}
