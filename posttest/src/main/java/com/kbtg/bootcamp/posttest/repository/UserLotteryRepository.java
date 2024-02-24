package com.kbtg.bootcamp.posttest.repository;

import com.kbtg.bootcamp.posttest.model.UserLottery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLotteryRepository extends JpaRepository<UserLottery, Integer> {

    List<UserLottery> findByUserId(String userId);

    @Query("select ul from UserLottery ul where ul.userId = :userId and ul.lottery.id = :ticketId")
    List<UserLottery> findByUserIdAndTicketId(String userId, String ticketId);
}
