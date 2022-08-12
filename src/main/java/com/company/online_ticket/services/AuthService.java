package com.company.online_ticket.services;

import com.company.online_ticket.configs.security.UserDetails;
import com.company.online_ticket.domains.AuthUser;
import com.company.online_ticket.dto.UserCreateDTO;
import com.company.online_ticket.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthUser authUser = authRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found!")
        );
        return new UserDetails(authUser);
    }

    public void register(UserCreateDTO userCreateDTO, BindingResult rs) {
        if (rs.hasErrors()) {
            return;
        }

        if (!userCreateDTO.getPassword().equals(userCreateDTO.getConfirmPassword())) {
            rs.addError(new FieldError("userCreateDTO", "confirmPassword", "Confirm password must be like password!"));
            return;
        }

        Optional<AuthUser> optionalAuthUser = authRepository.findByEmail(userCreateDTO.getEmail());
        if (optionalAuthUser.isPresent() && optionalAuthUser.get().getEmail().equals(userCreateDTO.getEmail())) {
            rs.addError(new FieldError("userCreateDTO", "email", "User already registered!"));
            return;
        }
        authRepository.save(AuthUser.builder()
                .email(userCreateDTO.getEmail())
                .phone(userCreateDTO.getPhone())
                .password(passwordEncoder.encode(userCreateDTO.getPassword()))
                .active(true)
                .build());
    }

    public Page<AuthUser> getAll(Pageable pageable) {
        List<AuthUser> users = authRepository.findAll();
        List<AuthUser> userList;
        int startItem = pageable.getPageNumber() * pageable.getPageSize();
        if (users.size() < startItem) {
            userList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageable.getPageSize(), users.size());
            userList = users.subList(startItem, toIndex);
        }
        return new PageImpl<>(userList, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), users.size());
    }

    public AuthUser getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = "";
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        return authRepository.findByEmail(email).orElseThrow(() -> {
            throw new RuntimeException("User not found!");
        });
    }

    public AuthUser get(Long id) {
        return authRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException("User not found!");
        });
    }
}
