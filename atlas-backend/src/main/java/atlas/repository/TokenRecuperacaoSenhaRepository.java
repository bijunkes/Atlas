package atlas.repository;

import atlas.entity.TokenRecuperacaoSenha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenRecuperacaoSenhaRepository
        extends JpaRepository<TokenRecuperacaoSenha, Long> {

    Optional<TokenRecuperacaoSenha> findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM TokenRecuperacaoSenha t WHERE t.usuario.id = :usuarioId")
    void deleteByUsuarioId(@Param("usuarioId") Long usuarioId);

    void deleteByExpiracaoBefore(LocalDateTime data);
}