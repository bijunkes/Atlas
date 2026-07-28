package atlas.repository;

import atlas.entity.InstituicaoFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstituicaoFinanceiraRepository extends JpaRepository<InstituicaoFinanceira, Long> {

    List<InstituicaoFinanceira> findByAtivoTrue();

}