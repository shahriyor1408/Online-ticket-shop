package com.company.online_ticket.repository;

import com.company.online_ticket.domains.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    @Query("from OrderHistory t where t.user.email = :emailSearch and t.statusHistory = 'NOT_ACCEPTED'")
    List<OrderHistory> findByUser(@Param("emailSearch") String emailSearch);

    @Query("from OrderHistory t where t.ticket.id = :idSearch and t.user.id = :id")
    OrderHistory findByTicketId(@Param("idSearch") Long idSearch, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query("update OrderHistory set quantity = :quantityUpdate where id = :idUpdate")
    void updateQuantity(@Param("idUpdate") Long idUpdate, @Param("quantityUpdate") Integer quantityUpdate);
}
