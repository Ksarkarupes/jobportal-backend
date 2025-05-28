package com.jobPortal.job_portal_backend.service;

import com.jobPortal.job_portal_backend.DTO.UserDTO;
import com.jobPortal.job_portal_backend.models.JobApplication;
import com.jobPortal.job_portal_backend.models.Jobs;
import com.jobPortal.job_portal_backend.models.User;
import com.jobPortal.job_portal_backend.repository.JobApplicationRepository;
import com.jobPortal.job_portal_backend.repository.JobRepository;
import com.jobPortal.job_portal_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    JobApplicationRepository jobApplicationRepository;

    public JobApplication applyForJob(int jobId, int applicantId) throws Exception{
        Optional<Jobs> jobsOptional = jobRepository.findById(jobId);
        Optional<User> applicantOptional = userRepository.findById((applicantId));

        if (jobsOptional.isEmpty()) throw new Exception("Job not found with job id: "+ jobId);
        if (applicantOptional.isEmpty()) throw new Exception("User not found with id: "+applicantId);

        Jobs job = jobsOptional.get();
        User applicant = applicantOptional.get();
        User employer = job.getEmployer();

        boolean alreadyApplied = jobApplicationRepository
                .findAll()
                .stream()
                .anyMatch(app -> app.getApplicant().getUserId() == (applicantId) && app.getJobs().getJobId() == (jobId));

        if(alreadyApplied) throw new Exception("You have already applied for this job");

        JobApplication jobApplication = new JobApplication();
        jobApplication.setApplicant(applicant);
        jobApplication.setEmployer(employer);
        jobApplication.setJobs(job);
        return jobApplicationRepository.save(jobApplication);
    }

    public List<UserDTO> getApplicantsForJob(int jobId, String employerEmail) throws Exception{
        Jobs job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
        if (!job.getEmployer().getEmail().equals(employerEmail)) throw new RuntimeException("You are not authorized to view applicants for this job");
        List<JobApplication> jobApplicationList = jobApplicationRepository.findByJobs(job);
        return jobApplicationList.stream().map(app -> new UserDTO(app.getApplicant())).collect(Collectors.toList());
    }
}
