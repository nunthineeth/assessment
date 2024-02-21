package com.kbtg.bootcamp.posttest.repository;

import com.kbtg.bootcamp.posttest.model.Lottery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LotteryRepository extends JpaRepository<Lottery, String> {

    @Query("SELECT l.ticketId FROM Lottery l WHERE l.isDeleted = false")
    List<String> findAllTickets();

    Optional<Lottery> findByTicketId(String id);
}
