package atlas.service;

import atlas.dto.transacao.TransacaoRequestDTO;
import atlas.dto.transacao.TransacaoResponseDTO;
import atlas.entity.Categoria;
import atlas.entity.Conta;
import atlas.entity.Transacao;
import atlas.entity.Usuario;
import atlas.enums.StatusTransacao;
import atlas.exception.ResourceNotFoundException;
import atlas.repository.CategoriaRepository;
import atlas.repository.ContaRepository;
import atlas.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    @Transactional
    public TransacaoResponseDTO criar(TransacaoRequestDTO dados) {

        validarDados(dados);
        validarValor(dados.valor());

        Usuario usuario =
                usuarioAutenticadoService.getUsuarioLogado();

        Conta conta =
                contaRepository.findByIdAndUsuario(
                                dados.contaId(),
                                usuario
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Conta não encontrada"
                                ));

        Categoria categoria = null;

        if(dados.categoriaId() != null){

            categoria =
                    categoriaRepository.buscarDisponivel(
                                    dados.categoriaId(),
                                    usuario
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Categoria não encontrada"
                                    ));
        }

        Transacao transacao = Transacao.builder()
                .usuario(usuario)
                .conta(conta)
                .categoria(categoria)
                .valor(dados.valor())
                .descricao(dados.descricao())
                .tipo(dados.tipo())
                .status(
                        dados.status() != null
                                ? dados.status()
                                : StatusTransacao.PAGO
                )
                .dataTransacao(dados.dataTransacao())
                .observacao(dados.observacao())
                .build();

        transacaoRepository.save(transacao);

        return toResponse(transacao);
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> listar(){

        Usuario usuario =
                usuarioAutenticadoService.getUsuarioLogado();


        return transacaoRepository
                .findByUsuarioAndExcluidoEmIsNull(usuario)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TransacaoResponseDTO buscarPorId(Long id){

        Usuario usuario =
                usuarioAutenticadoService.getUsuarioLogado();

        Transacao transacao =
                transacaoRepository
                        .findByIdAndUsuarioAndExcluidoEmIsNull(
                                id,
                                usuario
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transação não encontrada"
                                ));

        return toResponse(transacao);
    }

    private TransacaoResponseDTO toResponse(
            Transacao transacao
    ){

        return new TransacaoResponseDTO(
                transacao.getId(),
                transacao.getValor(),
                transacao.getDescricao(),
                transacao.getTipo(),
                transacao.getStatus(),
                transacao.getDataTransacao(),
                transacao.getCategoria() != null
                        ? transacao.getCategoria().getNome()
                        : null,
                transacao.getConta().getNome()
        );
    }

    @Transactional
    public TransacaoResponseDTO atualizar(Long id, TransacaoRequestDTO dados) {

        validarDados(dados);
        validarValor(dados.valor());

        Usuario usuario =
                usuarioAutenticadoService.getUsuarioLogado();

        Transacao transacao =
                transacaoRepository
                        .findByIdAndUsuarioAndExcluidoEmIsNull(id, usuario)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transação não encontrada"
                                ));

        Conta conta =
                contaRepository.findByIdAndUsuario(
                                dados.contaId(),
                                usuario
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Conta não encontrada"
                                ));

        Categoria categoria = null;

        if (dados.categoriaId() != null) {

            categoria =
                    categoriaRepository.buscarDisponivel(
                                    dados.categoriaId(),
                                    usuario
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Categoria não encontrada"
                                    ));
        }

        transacao.setConta(conta);
        transacao.setCategoria(categoria);
        transacao.setValor(dados.valor());
        transacao.setDescricao(dados.descricao());
        transacao.setTipo(dados.tipo());
        transacao.setStatus(
                dados.status() != null
                        ? dados.status()
                        : transacao.getStatus()
        );
        transacao.setDataTransacao(dados.dataTransacao());
        transacao.setObservacao(dados.observacao());

        return toResponse(transacaoRepository.save(transacao));
    }

    @Transactional
    public void excluir(Long id) {

        Usuario usuario =
                usuarioAutenticadoService.getUsuarioLogado();

        Transacao transacao =
                transacaoRepository
                        .findByIdAndUsuarioAndExcluidoEmIsNull(id, usuario)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transação não encontrada"
                                ));

        transacao.setExcluidoEm(LocalDateTime.now());

        transacaoRepository.save(transacao);
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> listarPorPeriodo(
            LocalDate inicio,
            LocalDate fim
    ) {

        Usuario usuario =
                usuarioAutenticadoService.getUsuarioLogado();


        return transacaoRepository
                .findByUsuarioAndDataTransacaoBetweenAndExcluidoEmIsNull(
                        usuario,
                        inicio,
                        fim
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void validarValor(BigDecimal valor){

        if(valor == null || valor.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException(
                    "O valor deve ser maior que zero"
            );
        }
    }

    private void validarDados(TransacaoRequestDTO dados){

        if(dados.tipo() == null){
            throw new IllegalArgumentException(
                    "O tipo da transação é obrigatório"
            );
        }

        if(dados.dataTransacao() == null){
            throw new IllegalArgumentException(
                    "A data da transação é obrigatória"
            );
        }

        if(dados.contaId() == null){
            throw new IllegalArgumentException(
                    "A conta é obrigatória"
            );
        }

        if(dados.dataTransacao().isAfter(LocalDate.now())){
            throw new IllegalArgumentException(
                    "A data da transação não pode estar no futuro"
            );
        }
    }
}
