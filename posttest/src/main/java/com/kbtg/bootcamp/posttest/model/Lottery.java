package com.kbtg.bootcamp.posttest.model;

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

@Entity
@Table(name = "lotteries")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Lottery {

    @Id
    @Column(name = "ticket_id")
    private String ticketId;

    private BigDecimal price;

    private int amount;

    private boolean isDeleted;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;
}
