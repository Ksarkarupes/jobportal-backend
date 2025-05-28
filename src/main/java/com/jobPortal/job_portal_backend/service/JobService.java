package com.jobPortal.job_portal_backend.service;

import com.jobPortal.job_portal_backend.models.Jobs;
import com.jobPortal.job_portal_backend.models.User;
import com.jobPortal.job_portal_backend.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {
    @Autowired
    JobRepository jobRepository;

    public List<Jobs> viewJobs(){
        return jobRepository.findAll();
    }
    public void addJob(Jobs jobs){
        jobRepository.save(jobs);
    }
    public List<Jobs> getJobsByEmployerId(int userId){
        return jobRepository.findByEmployerUserId(userId);
    }
}
