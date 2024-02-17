package com.example.polls.repository;


import com.example.polls.model.Poll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {
    Optional<Poll> findById(Long pollId);

    List<Poll> findByIdIn(List<Long> pollIds);

    List<Poll> findByIdIn(List<Long> pollIds, Sort sort);

    List<Poll> findAllByItem(String item);

    List<Poll> findAllByExpirationDateTime(Instant time);

    @Query ("SELECT item FROM Poll")
    List<String> getItems();

    //SELECT c FROM Admin4 a4 JOIN a4.choices c JOIN a4.admin3 a3 JOIN a3.admin2 a2 JOIN a2.admin1 WHERE a4.admin4Name_en = :city"

    @Query("SELECT p FROM Poll p WHERE p.item = :item")
    Poll findByItem(@Param("item") String item);
}
