package com.scc363.hospitalproject.datamodels.dtos;

import javax.validation.constraints.Email;

public class VerificationDTO {
    @Email
    private String email;

    private String code;

    public String email() {
        return email;
    }

    public String code() {
        return code;
    }
}
