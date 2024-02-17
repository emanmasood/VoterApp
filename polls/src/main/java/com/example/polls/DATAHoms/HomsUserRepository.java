package com.example.polls.DATAHoms;

import com.example.polls.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface HomsUserRepository extends JpaRepository<User,Long> {
}
