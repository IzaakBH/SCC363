package com.scc363.hospitalproject.datamodels.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class VerificationDTO {
    private String email;

    private String code;

    public String email() {
        return email;
    }

    public String code() {
        return code;
    }
}
