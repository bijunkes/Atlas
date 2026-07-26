package atlas.dto;

import atlas.entity.Role;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        Role role
) {}
