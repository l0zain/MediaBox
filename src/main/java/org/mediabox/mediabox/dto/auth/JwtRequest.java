package org.mediabox.mediabox.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtRequest {
    @NotNull
    private String email;
    @NotNull
    private String password;
}
