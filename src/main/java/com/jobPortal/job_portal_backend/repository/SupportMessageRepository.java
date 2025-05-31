package com.jobPortal.job_portal_backend.repository;

import com.jobPortal.job_portal_backend.models.SupportMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportMessageRepository extends JpaRepository<SupportMessage, Long> {
}

