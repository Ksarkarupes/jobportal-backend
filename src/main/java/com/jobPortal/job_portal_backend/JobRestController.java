package com.jobPortal.job_portal_backend;

import com.jobPortal.job_portal_backend.DTO.UserDTO;
import com.jobPortal.job_portal_backend.models.JobApplication;
import com.jobPortal.job_portal_backend.models.Jobs;
import com.jobPortal.job_portal_backend.models.User;
import com.jobPortal.job_portal_backend.service.JobApplicationService;
import com.jobPortal.job_portal_backend.service.JobService;
import com.jobPortal.job_portal_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "https://jobportal-frontend-two.vercel.app/")
public class JobRestController {

    @Autowired
    JobService jobService;

    @Autowired
    UserService userService;

    @Autowired
    JobApplicationService jobApplicationService;

    @GetMapping("jobs")
    public List<Jobs> viewJobs(){
        return jobService.viewJobs();
    }

    @PostMapping("submitjob/{userId}")
    public ResponseEntity<String> addJob(@RequestBody Jobs jobs, @PathVariable int userId){
        User employer = userService.findById(userId);
        if (employer == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        jobs.setEmployer(employer);
        jobService.addJob(jobs);
        return ResponseEntity.ok("Job posted successfully");
    }

    @PostMapping("registerUser")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User user){
        Map<String, Object> response = new HashMap<>();
        try {
            userService.addUser(user);
            response.put("success", true);
            response.put("message", "User registration successful");
            return  ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @PostMapping("loginUser")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            User authenticatedUser = userService.getUser(user); // now returns User
            System.out.println("Login successful");
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("user", authenticatedUser); // ✅ send user back
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.out.println("Login Unsuccessful");
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("employer/{userId}/jobs")
    public ResponseEntity<List<Jobs>> getJobsByEmployerId(@PathVariable int userId){
        List<Jobs> jobsList = jobService.getJobsByEmployerId(userId);
        return ResponseEntity.ok(jobsList);
    }

    @PostMapping("apply/{jobId}/{userId}")
    public ResponseEntity<Map<String, Object>> applyForJob(@PathVariable int jobId, @PathVariable int userId){
        Map<String, Object> response = new HashMap<>();
        try{
            JobApplication jobApplication = jobApplicationService.applyForJob(jobId,userId);
            response.put("success", true);
            response.put("message", "Successfully applied to this job. Best of Luck");
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("employer/applications/{jobId}")
    public ResponseEntity<?> getApplicantsForJob(@PathVariable int jobId, @RequestParam String email) {
        try{
            List<UserDTO> applicants = jobApplicationService.getApplicantsForJob(jobId, email);
            return ResponseEntity.ok(applicants);
        } catch (IllegalAccessException e) {
            // Employer tried accessing a job they don't own
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "You are not authorized to view applicants for this job."));
        } catch (NoSuchElementException e) {
            // Job ID not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Job not found."));
        } catch (Exception e) {
            // Catch-all fallback
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong. Please try again later."));
        }

    }
}
