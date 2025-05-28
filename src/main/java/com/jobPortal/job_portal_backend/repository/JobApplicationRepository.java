package com.jobPortal.job_portal_backend.repository;

import com.jobPortal.job_portal_backend.models.JobApplication;
import com.jobPortal.job_portal_backend.models.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    public List<JobApplication> findByJobs(Jobs jobs);
}
