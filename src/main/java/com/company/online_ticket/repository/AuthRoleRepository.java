package com.company.online_ticket.repository;


import com.company.online_ticket.domains.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRoleRepository extends JpaRepository<AuthRole, Long> {

}
