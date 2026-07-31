package atlas.service;

import atlas.repository.TokenRecuperacaoSenhaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenCleanupService {

    private final TokenRecuperacaoSenhaRepository repository;

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void limparTokensExpirados() {

        repository.deleteByExpiracaoBefore(LocalDateTime.now());

    }
}
