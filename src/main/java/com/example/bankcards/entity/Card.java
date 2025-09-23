package com.example.bankcards.entity;

import com.example.bankcards.model.CardStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cards")
public class Card {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_id_seq")
    @SequenceGenerator(name = "card_id_seq", sequenceName = "card_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "card_number", unique = true, nullable = false, length = 16)
    private String number;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_expiration")
    private Date dateOfExpiration;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "date_of_add")
    private Date dateOfAdd;

    @Column(name = "date_of_update")
    private Date dateOfUpdate;
}
