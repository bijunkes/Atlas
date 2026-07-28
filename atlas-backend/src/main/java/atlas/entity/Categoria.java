package atlas.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 7)
    private String cor;

    @Column(length = 50)
    private String icone;

    @Column(nullable = false)
    private Boolean padrao;

    @Column(nullable = false)
    private Boolean ativo;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    // Relacionamentos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonBackReference("usuario-categorias")
    private Usuario usuario;

    @OneToMany(mappedBy = "categoria")
    @JsonManagedReference
    @Builder.Default
    private List<Transacao> transacoes = new ArrayList<>();

    @OneToMany(mappedBy = "categoria")
    @JsonManagedReference
    @Builder.Default
    private List<Recorrencia> recorrencias = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        criadoEm = LocalDateTime.now();

        if (ativo == null) {
            ativo = true;
        }

        if (padrao == null) {
            padrao = false;
        }
    }

}