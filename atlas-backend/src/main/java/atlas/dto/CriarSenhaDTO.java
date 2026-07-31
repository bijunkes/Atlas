package atlas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarSenhaDTO(

        @NotBlank(message = "Senha obrigatória")
        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
        String senha

) {}
