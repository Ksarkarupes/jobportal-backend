package com.jobPortal.job_portal_backend.DTO;

import com.jobPortal.job_portal_backend.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String name;
    private String email;
    public UserDTO(User user){
        this.id = user.getUserId();
        this.name = user.getFullName();
        this.email = user.getEmail();
    }
}
