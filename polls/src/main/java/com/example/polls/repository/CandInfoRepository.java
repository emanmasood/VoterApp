package com.example.polls.repository;

import com.example.polls.model.Candinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandInfoRepository extends JpaRepository<Candinfo,Long> {

    @Query("SELECT p FROM Candinfo p WHERE p.name = :name")
    Candinfo findByName(@Param("name") String name);
}
