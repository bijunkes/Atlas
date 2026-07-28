package atlas.repository;

import atlas.entity.Recorrencia;
import atlas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RecorrenciaRepository extends JpaRepository<Recorrencia, Long> {

    List<Recorrencia> findByUsuarioAndAtivoTrue(Usuario usuario);

    List<Recorrencia> findByProximaExecucaoLessThanEqualAndAtivoTrue(LocalDate data);

}