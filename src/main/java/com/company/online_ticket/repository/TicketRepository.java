package com.company.online_ticket.repository;

import com.company.online_ticket.domains.Ticket;
import com.company.online_ticket.enums.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("from Ticket t where t.genre = :genreSearch")
    List<Ticket> findByGenre(@Param("genreSearch") Genre genre);

    @Query("from Ticket t where t.description like %:search% and t.title like %:search%")
    List<Ticket> find(@Param("search") String search);
}
