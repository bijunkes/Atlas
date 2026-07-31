package atlas.dto.categoria;

public record CategoriaResponseDTO(

        Long id,
        String nome,
        String cor,
        String icone,
        Boolean padrao,
        Boolean ativo
        
) {}
