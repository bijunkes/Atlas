package atlas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDTO {

    private String accessToken;

    private String refreshToken;

    private Long id;

    private String nome;

    private String email;

    private String role;

}