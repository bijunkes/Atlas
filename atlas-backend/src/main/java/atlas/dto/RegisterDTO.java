package atlas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDTO(

        @NotBlank(message = "Nome é obrigatório")
        String nome,


        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,


        @NotBlank(message = "Senha é obrigatória")
        @Size(
                min = 8,
                message = "A senha deve ter no mínimo 8 caracteres"
        )
        String senha

) {}