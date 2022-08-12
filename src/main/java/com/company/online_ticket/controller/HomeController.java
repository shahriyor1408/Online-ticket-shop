package com.company.online_ticket.controller;

import com.company.online_ticket.domains.Ticket;
import com.company.online_ticket.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final TicketService ticketService;

    @RequestMapping
    @PreAuthorize("permitAll()")
    public String homePage(@RequestParam("search") Optional<String> optionalSearch, @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size, @RequestParam("genre") Optional<String> optionalGenre, Model model) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(8);
        String search = "";
        if (optionalSearch.isPresent()) {
            search = optionalSearch.get();
        }
        Page<Ticket> ticketPage = ticketService.getAll(PageRequest.of(currentPage - 1, pageSize), search);
        if (optionalGenre.isPresent()) {
            ticketPage = ticketService.filterByGenre(optionalGenre.get());
        }
        model.addAttribute("page", ticketPage);

        int totalPages = ticketPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("numbers", pageNumbers);
        }
//        model.addAttribute("search", )
        return "index";
    }

    @RequestMapping("/showCover")
    @PreAuthorize("permitAll()")
    public void showCover(@RequestParam("img") String img, HttpServletResponse resp) {
        ServletOutputStream outputStream;
        try {
            outputStream = resp.getOutputStream();
            Path path = Path.of("/home/shahriyor/IdeaProjects/Online-ticket-shop-spring-mvc-security/apps/library" + img);
            Files.copy(path, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/basket", method = RequestMethod.GET)
    @PreAuthorize("permitAll()")
    public String basket(Model model) {
        model.addAttribute("orderHistories", ticketService.getOrderHistory());
        model.addAttribute("total", ticketService.getTotalSumOfOrder());
        return "basket";
    }

    @RequestMapping(value = "/addToCart/{id}", method = RequestMethod.GET)
    @PreAuthorize("permitAll()")
    public String addToCartPage(@PathVariable Long id, Model model) {
        model.addAttribute("ticket", ticketService.get(id));
        return "/addToCart";
    }

    @RequestMapping(value = "/addToCart/{id}", method = RequestMethod.POST)
    @PreAuthorize("permitAll()")
    public String addToCart(@PathVariable Long id, @RequestParam("quantity") Optional<Integer> quantity) {
        ticketService.addToCart(id, quantity);
        return "redirect:/";
    }

    @RequestMapping(value = "/ticketDetails/{id}", method = RequestMethod.GET)
    @PreAuthorize("permitAll()")
    public String ticketDetails(@PathVariable Long id, Model model) {
        model.addAttribute("ticket", ticketService.get(id));
        return "ticketDetails";
    }

    @RequestMapping(value = "/deleteOrder/{id}")
    public String deleteOrder(@PathVariable Long id) {
        ticketService.deleteOrder(id);
        return "redirect:/basket";
    }
}
