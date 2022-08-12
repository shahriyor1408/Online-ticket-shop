package com.company.online_ticket.domains;

import com.company.online_ticket.enums.StatusHistory;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Builder
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Ticket ticket;

    @OneToOne
    private AuthUser user;
    private Timestamp localDateTime;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private StatusHistory statusHistory;
}
