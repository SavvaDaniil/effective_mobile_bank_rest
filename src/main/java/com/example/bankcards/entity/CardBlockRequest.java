package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "card_block_requests")
public class CardBlockRequest {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_block_request_id_seq")
    @SequenceGenerator(name = "card_block_request_id_seq", sequenceName = "card_block_request_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(name = "date_of_add")
    private Date dateOfAdd;

    @Column(name = "date_of_blocked")
    private Date dateOfBlocked;

}
