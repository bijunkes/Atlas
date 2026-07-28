package atlas.repository;

import atlas.entity.Categoria;
import atlas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query("""
        SELECT c FROM Categoria c
        WHERE (c.usuario = :usuario OR c.padrao = true)
        AND c.ativo = true
    """)
    List<Categoria> listarDisponiveisParaUsuario(
            Usuario usuario
    );

    @Query("""
        SELECT c FROM Categoria c
        WHERE c.id = :id
        AND (c.usuario = :usuario OR c.padrao = true)
    """)
    Optional<Categoria> buscarDisponivel(
            Long id,
            Usuario usuario
    );

    Optional<Categoria> findByIdAndUsuario(Long id, Usuario usuario);

}