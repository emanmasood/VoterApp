package com.example.polls.repository;

import com.example.polls.model.Admin2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Admin2Repository extends JpaRepository<Admin2, Integer> {

    @Query("SELECT a12 FROM Admin1 a1 JOIN a1.admin2s a12 WHERE a12.admin1.id = :id")
    List<Admin2> findAllByAdmin1Id(@Param("id") Integer id);
}
