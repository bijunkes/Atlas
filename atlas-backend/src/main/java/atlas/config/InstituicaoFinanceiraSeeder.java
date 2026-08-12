package atlas.config;

import atlas.entity.InstituicaoFinanceira;
import atlas.repository.InstituicaoFinanceiraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InstituicaoFinanceiraSeeder implements CommandLineRunner {

        private final InstituicaoFinanceiraRepository repository;

        @Override
        public void run(String... args) {

                if (repository.count() > 0) {
                        return;
                }

                List<InstituicaoFinanceira> instituicoes = List.of(

                                InstituicaoFinanceira.builder()
                                                .nome("Banco Inter")
                                                .codigoBanco("077")
                                                .build(),

                                InstituicaoFinanceira.builder()
                                                .nome("Nubank")
                                                .codigoBanco("260")
                                                .build(),

                                InstituicaoFinanceira.builder()
                                                .nome("Itaú Unibanco")
                                                .codigoBanco("341")
                                                .build(),

                                InstituicaoFinanceira.builder()
                                                .nome("Bradesco")
                                                .codigoBanco("237")
                                                .build(),

                                InstituicaoFinanceira.builder()
                                                .nome("Banco do Brasil")
                                                .codigoBanco("001")
                                                .build(),

                                InstituicaoFinanceira.builder()
                                                .nome("Caixa Econômica Federal")
                                                .codigoBanco("104")
                                                .build());

                repository.saveAll(instituicoes);
        }
}