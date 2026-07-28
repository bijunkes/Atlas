package atlas.repository;

import atlas.entity.Transacao;
import atlas.entity.Usuario;
import atlas.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByUsuarioAndExcluidoEmIsNull(
            Usuario usuario
    );

    List<Transacao> findByContaAndExcluidoEmIsNull(
            Conta conta
    );

    List<Transacao> findByUsuarioAndDataTransacaoBetweenAndExcluidoEmIsNull(
            Usuario usuario,
            LocalDate inicio,
            LocalDate fim
    );

    Optional<Transacao> findByIdAndUsuarioAndExcluidoEmIsNull(
            Long id,
            Usuario usuario
    );
}