package atlas.service;

import atlas.entity.InstituicaoFinanceira;
import atlas.repository.InstituicaoFinanceiraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstituicaoFinanceiraService {

    private final InstituicaoFinanceiraRepository repository;

    public List<InstituicaoFinanceira> listarAtivas() {
        return repository.findByAtivoTrue();
    }
}