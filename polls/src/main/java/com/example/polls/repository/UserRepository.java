package com.example.polls.repository;

import com.example.polls.model.RoleName;
import com.example.polls.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsernameOrEmail(String username, String email);



    @Query("SELECT r.name FROM User u JOIN u.roles r WHERE u.id = :userid")
    RoleName roleName (@Param("userid") Long userid);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findByEmail(String username);
}
