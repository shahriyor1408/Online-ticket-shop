package com.company.online_ticket.domains;

import com.company.online_ticket.enums.Genre;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private LocalDateTime date;
    private Integer price;

    private Integer count;

    @OneToOne
    private Upload cover;
}
