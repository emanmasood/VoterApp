package com.example.polls.DATADamas;

import com.example.polls.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface DamasUserRepository extends JpaRepository<User,Long> {
}
