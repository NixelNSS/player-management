package com.kosticnikola.transfer.repository;

import com.kosticnikola.transfer.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    List<Transfer> findAllByPlayerId(Long playerId);

    Optional<Transfer> findFirstByPlayerIdOrderByCreatedAtAsc(Long playerId);

    Optional<Transfer> findFirstByPlayerIdOrderByCreatedAtDesc(Long playerId);

}
