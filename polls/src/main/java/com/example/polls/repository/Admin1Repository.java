package com.example.polls.repository;

import com.example.polls.model.Admin1;
import com.example.polls.model.Admin2;
import com.example.polls.model.LocalChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface Admin1Repository extends JpaRepository<Admin1, Integer> {

    @Query("SELECT c FROM Admin4 a4 JOIN a4.choices c WHERE c.admin4.id = :id")
    List<LocalChoice> getChoicesByAdmin4(@Param("id") Integer id);
}
