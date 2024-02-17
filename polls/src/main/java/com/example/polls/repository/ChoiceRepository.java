package com.example.polls.repository;

import com.example.polls.model.Choice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChoiceRepository extends JpaRepository<Choice, Long> {

    boolean existsByText(String text);

    Choice findByText(String text);
}
