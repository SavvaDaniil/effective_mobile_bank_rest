package com.example.bankcards.repository;

import com.example.bankcards.entity.CardTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardTransferRepository extends JpaRepository<CardTransfer, Long> {

}
