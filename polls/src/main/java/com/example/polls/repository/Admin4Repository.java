package com.example.polls.repository;

import com.example.polls.model.Admin4;
import com.example.polls.model.Choice;
import com.example.polls.model.LocalChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Admin4Repository extends JpaRepository<Admin4, Integer> {

    //@Query("SELECT c FROM Admin4 a4 JOIN a4.choices c JOIN  WHERE a12.admin1.id = :id) // AND a4.admin4Name_en = :city //
    @Query("SELECT a4 FROM Admin4 a4 JOIN FETCH a4.admin3 a3 JOIN FETCH a3.admin2 a2 JOIN FETCH a2.admin1 a1 WHERE a4.admin4Name_en = :city")
    Admin4 getAdmin4(@Param("city") String city);
}
