package org.mediabox.mediabox.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mediabox.mediabox.entity.Role;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;
}
