package atlas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetarSenhaDTO(

        @NotBlank
        String token,

        @NotBlank
        @Size(min = 8)
        String novaSenha

) {}