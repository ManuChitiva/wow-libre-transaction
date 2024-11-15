package com.wow.libre.infrastructure.repositories.transaction;

import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface TransactionRepository extends CrudRepository<TransactionEntity, Long> {
    Optional<TransactionEntity> findByReferenceNumber(String referenceNumber);

    Optional<TransactionEntity> findByPaymentId(String paymentId);

    Page<TransactionEntity> findByUserIdOrderByCreationDateDesc(Long userId, Pageable pageable);

    @Query("SELECT COUNT(a) FROM TransactionEntity a WHERE a.userId = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT a FROM TransactionEntity a WHERE (a.status = :status OR a.status = :secondStatus) AND a.send = " +
            "false")
    List<TransactionEntity> findByStatusAndSendIsFalse(@Param("status") String status,
                                                       @Param("secondStatus") String secondStatus);


}
