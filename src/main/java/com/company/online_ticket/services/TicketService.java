package com.company.online_ticket.services;

import com.company.online_ticket.domains.AuthUser;
import com.company.online_ticket.domains.OrderHistory;
import com.company.online_ticket.domains.Ticket;
import com.company.online_ticket.domains.Upload;
import com.company.online_ticket.dto.TicketDTO;
import com.company.online_ticket.dto.TicketUpdateDTO;
import com.company.online_ticket.enums.Genre;
import com.company.online_ticket.enums.StatusHistory;
import com.company.online_ticket.repository.OrderHistoryRepository;
import com.company.online_ticket.repository.TicketRepository;
import com.company.online_ticket.repository.UploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final Path rootPath = Path.of("/home/shahriyor/IdeaProjects/Online-ticket-shop-spring-mvc-security/apps/library/uploads");
    private final UploadRepository uploadRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final AuthService authService;

    public void save(TicketDTO ticketDTO, BindingResult rs, MultipartFile file) {
        if (rs.hasErrors()) {
            return;
        }
//        Upload archive = getArchiveCover();
        Ticket ticket = Ticket.builder()
                .title(ticketDTO.getTitle())
                .description(ticketDTO.getDescription())
                .price(ticketDTO.getPrice())
                .date(LocalDateTime.parse(ticketDTO.getDate()))
                .count(ticketDTO.getCount())
                .genre(Enum.valueOf(Genre.class, ticketDTO.getGenre()))
                .cover(uploadCover(file))
                .build();
        ticketRepository.save(ticket);
    }

    private Upload uploadCover(MultipartFile file) {
        try {
            String contentType = file.getContentType();
            String originalFilename = file.getOriginalFilename();
            long size = file.getSize();
            String filename = StringUtils.getFilename(originalFilename);
            String filenameExtension = StringUtils.getFilenameExtension(originalFilename);
            String generatedName = System.currentTimeMillis() + "." + filenameExtension;
            String path = "/uploads/" + generatedName;
            Upload uploads = Upload
                    .builder()
                    .contentType(contentType)
                    .originalName(filename)
                    .size(size)
                    .generatedName(generatedName)
                    .path(path)
                    .build();
            uploadRepository.save(uploads);
            Path uploadPath = rootPath.resolve(generatedName);
            Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
            return uploads;
        } catch (IOException e) {
            throw new RuntimeException("Something wrong try again");
        }
    }

    public Page<Ticket> getAll(Pageable pageable, String search) {
        List<Ticket> tickets = ticketRepository.find(search);
        List<Ticket> ticketList;

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        if (tickets.size() < startItem) {
            ticketList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, tickets.size());
            ticketList = tickets.subList(startItem, toIndex);
        }
        return new PageImpl<>(ticketList, PageRequest.of(currentPage, pageSize), tickets.size());
    }

    public Ticket get(Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException("Ticket not found!");
        });
    }

    public void update(TicketUpdateDTO ticketUpdateDTO) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketUpdateDTO.getId());
        if (optionalTicket.isEmpty()) {
            throw new RuntimeException("Ticket not fond!");
        }
        ticketRepository.save(Ticket.builder()
                .id(ticketUpdateDTO.getId())
                .title(ticketUpdateDTO.getTitle())
                .description(ticketUpdateDTO.getDescription())
                .price(ticketUpdateDTO.getPrice())
                .date(LocalDateTime.parse(ticketUpdateDTO.getDate()))
                .genre(Genre.valueOf(ticketUpdateDTO.getGenre()))
                .count(ticketUpdateDTO.getCount())
                .cover(optionalTicket.get().getCover())
                .build());
    }

    public void delete(Long id) {
        ticketRepository.deleteById(id);
    }

    public Page<Ticket> filterByGenre(String genre) {
        return new PageImpl<>(ticketRepository.findByGenre(Genre.valueOf(genre.toUpperCase())));
    }

    public void addToCart(Long id, Optional<Integer> quantity) {
        AuthUser user = authService.getCurrentUser();
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException("Ticket not found!");
        });
        OrderHistory orderHistory = orderHistoryRepository.findByTicketId(id, user.getId());
        if (Objects.nonNull(orderHistory)) {
//            orderHistory.setQuantity(orderHistory.getQuantity() + quantity.orElse(1));
//            orderHistoryRepository.save(orderHistory);
            orderHistoryRepository.updateQuantity(orderHistory.getId(), orderHistory.getQuantity() + quantity.orElse(1));
        } else {
            orderHistoryRepository.save(OrderHistory.builder()
                    .ticket(ticket)
                    .user(user)
                    .statusHistory(StatusHistory.NOT_ACCEPTED)
                    .localDateTime(Timestamp.valueOf(LocalDateTime.now()))
                    .quantity(quantity.orElse(1))
                    .build());
        }
    }

    public List<OrderHistory> getOrderHistory() {
        AuthUser user = authService.getCurrentUser();
        return orderHistoryRepository.findByUser(user.getEmail());
    }

    public List<OrderHistory> getAllOrderHistory() {
        return orderHistoryRepository.findAll();
    }

    public Integer getTotalSumOfOrder() {
        List<OrderHistory> histories = getOrderHistory();
        int sum = 0;
        for (OrderHistory history : histories) {
            sum += history.getTicket().getPrice() * history.getQuantity();
        }
        return sum;
    }

    public void deleteOrder(Long id) {
        orderHistoryRepository.deleteById(id);
    }

//    private Upload getArchiveCover() {
//        return Upload.builder()
//                .contentType(".png")
//                .size(52736)
//                .path("apps/library/archive/tickets-inverse.png")
//                .generatedName("archive")
//                .originalName("archive")
//                .build();
//    }
}
