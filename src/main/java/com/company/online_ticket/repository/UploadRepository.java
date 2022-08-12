package com.company.online_ticket.repository;

import com.company.online_ticket.domains.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadRepository extends JpaRepository<Upload, Long> {
}
