package com.company.online_ticket.dto;

import com.company.online_ticket.annotations.ValidLocalDateTime;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDTO {
    @NotEmpty(message = "Title can not be empty")
    private String title;

    @NotEmpty(message = "Title can not be empty")
    @Size(min = 10, max = 100, message = "Size must be min=10 and max=25")
    private String description;
    private String genre;

    @ValidLocalDateTime(message = "value for expiration field invalid")
    private String date;

    @NotNull(message = "Price can not be empty")
    @Min(value = 5000, message = "Price must be min=5000 and max=1_000_000")
    @Max(value = 1_000_000, message = "Price must be min=5000 and max=1_000_000")
    private Integer price;

    @NotNull(message = "Count can not be empty")
    @Min(value = 20, message = "Min value must be 20")
    @Max(value = 100, message = "Max value must be 100")
    private Integer count;

    MultipartFile file;
}
