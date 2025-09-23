package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import org.hibernate.query.spi.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByNumber(String number);

    @Query(value = "SELECT c FROM Card c WHERE c.number LIKE %?1% AND c.user.id = ?2 ORDER BY id DESC")
    List<Card> searchByQueryAndUserId(String searchQuery, Long userId, Limit limit);

    @Query(value = "SELECT c FROM Card c WHERE c.number LIKE %?1% ORDER BY id DESC")
    List<Card> searchByQuery(String searchQuery, Limit limit);
}
