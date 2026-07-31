package atlas.repository;

import atlas.entity.Conta;
import atlas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    List<Conta> findByUsuario(Usuario usuario);

    List<Conta> findByUsuarioAndAtivoTrue(Usuario usuario);

    Optional<Conta> findByIdAndUsuario(Long id, Usuario usuario);

    Optional<Conta> findByIdAndUsuarioAndAtivoTrue(
            Long id,
            Usuario usuario);

}