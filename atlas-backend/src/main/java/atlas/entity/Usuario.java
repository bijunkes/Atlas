package atlas.entity;

import atlas.enums.Role;
import atlas.enums.AuthProvider;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @JsonIgnore
    @Column(nullable = true)
    private String senha;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "imagem_perfil", length = 255)
    private String imagemPerfil;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    private String googleId;

    // Relacionamentos
    @OneToMany(mappedBy = "usuario")
    @JsonManagedReference("usuario-contas")
    @Builder.Default
    private List<Conta> contas = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    @JsonManagedReference("usuario-categorias")
    @Builder.Default
    private List<Categoria> categorias = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    @JsonManagedReference("usuario-transacoes")
    @Builder.Default
    private List<Transacao> transacoes = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    @JsonManagedReference("usuario-recorrencias")
    @Builder.Default
    private List<Recorrencia> recorrencias = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        criadoEm = LocalDateTime.now();

        if (role == null) {
            role = Role.USER;
        }

        if (provider == null) {
            provider = AuthProvider.LOCAL;
        }
    }

    @PreUpdate
    public void preUpdate() {
        atualizadoEm = LocalDateTime.now();
    }

}
