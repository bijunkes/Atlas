package atlas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RecuperarSenhaDTO(

        @Email
        @NotBlank
        String email
        
) {}
