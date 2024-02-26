package com.kbtg.bootcamp.posttest.repository;

import com.kbtg.bootcamp.posttest.model.Lottery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LotteryRepository extends JpaRepository<Lottery, String> {

    @Query("SELECT l FROM Lottery l where l.amount > 0")
    List<Lottery> findRemainingTickets();

    Optional<Lottery> findById(String id);
}
