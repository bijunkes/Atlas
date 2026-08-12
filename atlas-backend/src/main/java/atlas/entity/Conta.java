package atlas.entity;

import atlas.enums.TipoConta;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoConta tipo;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal saldoInicial;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal saldoAtual;

    private String numeroAgencia;
    private String numeroConta;

    private LocalDateTime criadaEm;
    private LocalDateTime atualizadoEm;

    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;

    // Relacionamentos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference("usuario-contas")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instituicao_id")
    @JsonBackReference("instituicao-contas")
    private InstituicaoFinanceira instituicao;

    @OneToMany(mappedBy = "contaOrigem")
    @JsonManagedReference("conta-transferencias-enviadas")
    @Builder.Default
    private List<Transferencia> transferenciasEnviadas = new ArrayList<>();

    @OneToMany(mappedBy = "contaDestino")
    @JsonManagedReference("conta-transferencias-recebidas")
    @Builder.Default
    private List<Transferencia> transferenciasRecebidas = new ArrayList<>();

    @OneToMany(mappedBy = "conta")
    @JsonManagedReference
    @Builder.Default
    private List<Recorrencia> recorrencias = new ArrayList<>();

    @PrePersist
    protected void aoCriar() {
        criadaEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();

        if (saldoInicial == null) {
            saldoInicial = BigDecimal.ZERO;
        }

        if (saldoAtual == null) {
            saldoAtual = saldoInicial;
        }

        if (ativo == null) {
            ativo = true;
        }
    }

    @PreUpdate
    protected void aoAtualizar() {
        atualizadoEm = LocalDateTime.now();
    }

}