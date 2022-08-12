package com.company.online_ticket.controller;

import com.company.online_ticket.domains.AuthUser;
import com.company.online_ticket.domains.Ticket;
import com.company.online_ticket.dto.TicketDTO;
import com.company.online_ticket.dto.TicketUpdateDTO;
import com.company.online_ticket.enums.Genre;
import com.company.online_ticket.services.AuthService;
import com.company.online_ticket.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    public final AuthService authService;
    public final TicketService ticketService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPage(Model model, @RequestParam("page") Optional<Integer> page,
                            @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);
        Page<AuthUser> userPage = authService.getAll(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("page", userPage);
        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("number", pageNumbers);
        }
        return "admin/adminPage";
    }

    @GetMapping("/tickets")
    @PreAuthorize("hasRole('ADMIN')")
    public String ticketsPage(Model model,
                              @RequestParam("search") Optional<String> optionalSearch,
                              @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);
        String search = "";
        if (optionalSearch.isPresent()) {
            search = optionalSearch.get();
        }
        Page<Ticket> ticketPage = ticketService.getAll(PageRequest.of(currentPage - 1, pageSize), search);
        model.addAttribute("page", ticketPage);
        int totalPages = ticketPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/tickets";
    }

    @PostMapping("/tickets")
    @PreAuthorize("hasRole('ADMIN')")
    public String tickets() {
        return "admin/adminPage";
    }

    @GetMapping("/addTicket")
    @PreAuthorize("hasRole('ADMIN')")
    public String addTicketPage(Model model) {
        model.addAttribute("ticketDTO", new TicketDTO());
        model.addAttribute("genres", Genre.values());
        return "admin/addTicket";
    }

    @PostMapping(value = "/addTicket")
    @PreAuthorize("hasRole('ADMIN')")
    public String addTicket(@Valid @ModelAttribute("ticketDTO") TicketDTO ticketDTO,
                            BindingResult rs) {
        ticketService.save(ticketDTO, rs, ticketDTO.getFile());
        if (rs.hasErrors()) {
            return "admin/addTicket";
        }
        return "redirect:tickets";
    }

    @GetMapping(value = "/updateTicket/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateTicketPage(@PathVariable Long id, Model model) {
        model.addAttribute("ticket", ticketService.get(id));
        model.addAttribute("genres", Genre.values());
        return "admin/updateTicket";
    }

    @PostMapping(value = "/updateTicket")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateTicket(@ModelAttribute TicketUpdateDTO ticketUpdateDTO) {
        ticketService.update(ticketUpdateDTO);
        return "redirect:tickets";
    }

    @GetMapping(value = "/deleteTicket/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteTicketPage(@PathVariable Long id, Model model) {
        model.addAttribute("ticket", ticketService.get(id));
        return "admin/deleteTicket";
    }

    @PostMapping(value = "/deleteTicket/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteTicket(@PathVariable Long id) {
        ticketService.delete(id);
        return "redirect:tickets";
    }

    @GetMapping(value = "/orderHistory")
    @PreAuthorize("hasRole('ADMIN')")
    public String orderHistoryPage(Model model) {
        model.addAttribute("orderHistories", ticketService.getAllOrderHistory());
        return "/admin/orderHistory";
    }

    @GetMapping(value = "/userDetails/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String userDetailsPage(@PathVariable Long id, Model model) {
        model.addAttribute("user", authService.get(id));
        return "admin/userDetails";
    }
}
