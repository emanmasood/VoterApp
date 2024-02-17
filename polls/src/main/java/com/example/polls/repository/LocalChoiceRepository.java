package com.example.polls.repository;

import com.example.polls.model.LocalChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalChoiceRepository extends JpaRepository<LocalChoice, Long> {

    @Query("SELECT c FROM LocalChoice c JOIN FETCH c.admin4 a4 JOIN FETCH a4.admin3 a3 JOIN FETCH a3.admin2 a2 JOIN FETCH a2.admin1 a1 WHERE a4.admin4Name_en = :city")
    List<LocalChoice> get(@Param("city") String city);
}
