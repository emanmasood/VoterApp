package com.example.polls.repository;

import com.example.polls.model.Telecome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelecomeRepository extends JpaRepository<Telecome,Long> {

    boolean existsByIdnum(Integer idnum);

    boolean existsByCity(String City);

    Telecome findByIdnum(Integer idnum);
}
