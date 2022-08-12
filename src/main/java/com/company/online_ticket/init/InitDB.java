package com.company.online_ticket.init;

import com.company.online_ticket.domains.AuthRole;
import com.company.online_ticket.domains.AuthUser;
import com.company.online_ticket.repository.AuthPermissionRepository;
import com.company.online_ticket.repository.AuthRepository;
import com.company.online_ticket.repository.AuthRoleRepository;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
public class InitDB implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        AuthRoleRepository authRoleRepository = context.getBean(AuthRoleRepository.class);
        AuthPermissionRepository authPermissionRepository = context.getBean(AuthPermissionRepository.class);
        AuthRepository repository = context.getBean(AuthRepository.class);
        authPermissionRepository.deleteAll();
        authRoleRepository.deleteAll();
        repository.deleteAll();
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        AuthUser superUser = new AuthUser();
        superUser.setEmail("admin@123");
        superUser.setPassword(passwordEncoder.encode("1234"));
        superUser.setPhone("998991234567");
        superUser.setActive(true);

        AuthRole admin = new AuthRole();
        admin.setCode("ADMIN");
        admin.setName("Admin");

        superUser.getRoles().add(admin);
        repository.save(superUser);
    }
}
