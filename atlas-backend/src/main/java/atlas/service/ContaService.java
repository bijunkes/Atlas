package atlas.service;

import atlas.dto.conta.ContaRequestDTO;
import atlas.dto.conta.ContaResponseDTO;
import atlas.entity.Conta;
import atlas.entity.InstituicaoFinanceira;
import atlas.entity.Usuario;
import atlas.exception.ResourceNotFoundException;
import atlas.repository.ContaRepository;
import atlas.repository.InstituicaoFinanceiraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;
    private final InstituicaoFinanceiraRepository instituicaoRepository;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public ContaResponseDTO criar(ContaRequestDTO dados) {

        Usuario usuario =
                usuarioAutenticadoService.getUsuarioLogado();

        InstituicaoFinanceira instituicao = null;

        if (dados.instituicaoId() != null) {

            instituicao =
                    instituicaoRepository.findById(dados.instituicaoId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Instituição não encontrada"
                                    ));
        }

        Conta conta = Conta.builder()
                .usuario(usuario)
                .instituicao(instituicao)
                .nome(dados.nome())
                .tipo(dados.tipo())
                .saldoInicial(dados.saldoInicial())
                .numeroAgencia(dados.numeroAgencia())
                .numeroConta(dados.numeroConta())
                .ativo(true)
                .build();

        contaRepository.save(conta);

        return toResponse(conta);
    }

    public List<ContaResponseDTO> listarMinhasContas() {

        Usuario usuario =
                usuarioAutenticadoService.getUsuarioLogado();

        return contaRepository
                .findByUsuarioAndAtivoTrue(usuario)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ContaResponseDTO buscarPorId(Long id) {

        Usuario usuario =
                usuarioAutenticadoService.getUsuarioLogado();

        Conta conta =
                contaRepository.findByIdAndUsuario(id, usuario)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Conta não encontrada"
                                ));

        return toResponse(conta);
    }

    private ContaResponseDTO toResponse(Conta conta) {

        return new ContaResponseDTO(
                conta.getId(),
                conta.getNome(),
                conta.getTipo(),
                conta.getSaldoInicial(),
                conta.getInstituicao() != null
                        ? conta.getInstituicao().getNome()
                        : null,
                conta.getAtivo()
        );
    }

    public ContaResponseDTO atualizar(Long id, ContaRequestDTO dados) {

        Usuario usuario =
                usuarioAutenticadoService.getUsuarioLogado();

        Conta conta =
                contaRepository.findByIdAndUsuario(id, usuario)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Conta não encontrada"
                                ));

        InstituicaoFinanceira instituicao = null;

        if (dados.instituicaoId() != null) {

            instituicao =
                    instituicaoRepository.findById(dados.instituicaoId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Instituição não encontrada"
                                    ));
        }

        conta.setNome(dados.nome());
        conta.setTipo(dados.tipo());
        conta.setInstituicao(instituicao);
        conta.setNumeroAgencia(dados.numeroAgencia());
        conta.setNumeroConta(dados.numeroConta());

        contaRepository.save(conta);

        return toResponse(conta);
    }

    public void desativar(Long id) {

        Usuario usuario =
                usuarioAutenticadoService.getUsuarioLogado();

        Conta conta =
                contaRepository.findByIdAndUsuario(id, usuario)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Conta não encontrada"
                                ));

        conta.setAtivo(false);

        contaRepository.save(conta);
    }

    public void reativar(Long id) {

        Usuario usuario =
                usuarioAutenticadoService.getUsuarioLogado();

        Conta conta =
                contaRepository.findByIdAndUsuario(id, usuario)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Conta não encontrada"
                                ));

        conta.setAtivo(true);

        contaRepository.save(conta);
    }
}