package atlas.dto;

import atlas.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDTO {

    private String accessToken;
    private String refreshToken;
    private Usuario usuario;

}