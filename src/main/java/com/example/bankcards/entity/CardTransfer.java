package com.example.bankcards.entity;

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
@Table(name = "card_transfers")
public class CardTransfer {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_transfer_id_seq")
    @SequenceGenerator(name = "card_transfer_id_seq", sequenceName = "card_transfer_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "card_id_from", nullable = false)
    private Card cardFrom;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "card_id_to", nullable = false)
    private Card cardTo;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "date_of_add")
    private Date dateOfAdd;
}
