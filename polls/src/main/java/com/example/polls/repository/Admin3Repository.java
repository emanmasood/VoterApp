package com.example.polls.repository;

import com.example.polls.model.Admin3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Admin3Repository extends JpaRepository<Admin3, Integer> {

    @Query("SELECT a23 FROM Admin2 a2 JOIN a2.admin3s a23 WHERE a23.admin2.id = :id")
    List<?> findAllByAdmin2Id(@Param("id") Integer id);
}
