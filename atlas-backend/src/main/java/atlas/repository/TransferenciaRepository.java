package atlas.repository;

import atlas.entity.Transferencia;
import atlas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    List<Transferencia> findByUsuario(Usuario usuario);

}