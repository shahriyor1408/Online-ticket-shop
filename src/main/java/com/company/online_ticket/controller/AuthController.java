package com.company.online_ticket.controller;

import com.company.online_ticket.dto.UserCreateDTO;
import com.company.online_ticket.dto.UserLoginDTO;
import com.company.online_ticket.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    public final AuthService authService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("userLoginDTO", new UserLoginDTO());
        if (Objects.nonNull(error)) {
            model.addAttribute("error", error);
        }
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("userLoginDTO") UserLoginDTO userLoginDTO) {
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "auth/logout";
    }

    @GetMapping("/register")
    public String registerPage(@ModelAttribute("userCreateDTO") UserCreateDTO userCreateDTO) {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userCreateDTO") UserCreateDTO userCreateDTO,
                           BindingResult rs) {
        authService.register(userCreateDTO, rs);
        if (rs.hasErrors()) {
            return "auth/register";
        }
        return "redirect:auth/login";
    }
}
