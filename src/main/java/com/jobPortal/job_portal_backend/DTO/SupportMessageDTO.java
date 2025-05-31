package com.jobPortal.job_portal_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupportMessageDTO {
    private String name;
    private String email;
    private String message;
}
