package atlas.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instituicoes_financeiras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstituicaoFinanceira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 20)
    private String codigoBanco;

    private String logo;

    @Column(nullable = false)
    private Boolean ativo = true;

    // Relacionamentos
    @OneToMany(mappedBy = "instituicao")
    @JsonManagedReference("instituicao-contas")
    @Builder.Default
    private List<Conta> contas = new ArrayList<>();

}
