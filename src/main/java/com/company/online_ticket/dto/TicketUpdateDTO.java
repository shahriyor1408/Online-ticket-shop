package com.company.online_ticket.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketUpdateDTO {
    private Long id;
    //    @NotEmpty(message = "Title can not be empty")
    private String title;

    //    @NotEmpty(message = "Title can not be empty")
//    @Size(min = 10, max = 25, message = "Size must be min=10 and max=25")
    private String description;
    private String genre;

    //    @ValidLocalDateTime(message = "value for expiration field invalid")
    private String date;

    //    @NotNull(message = "Price can not be empty")
//    @Min(value = 5000, message = "Price must be min=5000 and max=1_000_000")
//    @Max(value = 1_000_000, message = "Price must be min=5000 and max=1_000_000")
    private Integer price;
    private Integer count;
}
