package com.kbtg.bootcamp.posttest.model;

import com.kbtg.bootcamp.posttest.exception.BusinessValidationException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.kbtg.bootcamp.posttest.utils.Constants.TICKETS_HAVE_BEEN_SOLD_OUT;

@Entity
@Table(name = "lotteries")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Lottery {

    @Id
    @Column(name = "ticket_id", unique = true, nullable = false, length = 6)
    private String id;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "amount", nullable = false)
    private int amount;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;

    public Lottery buyLottery(int unit){
        if (this.amount == 0) {
            throw new BusinessValidationException(TICKETS_HAVE_BEEN_SOLD_OUT);
        }

        this.amount = this.amount - unit;
        return this;
    }

    public Lottery sellBackLottery(int unit){
        this.amount = this.amount + unit;
        return this;
    }
}
