package atlas.service;

import atlas.dto.categoria.CategoriaRequestDTO;
import atlas.dto.categoria.CategoriaResponseDTO;
import atlas.entity.Categoria;
import atlas.entity.Usuario;
import atlas.exception.ResourceNotFoundException;
import atlas.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

        private final CategoriaRepository categoriaRepository;
        private final UsuarioAutenticadoService usuarioAutenticadoService;

        public CategoriaResponseDTO criar(CategoriaRequestDTO dados) {

                Usuario usuario = usuarioAutenticadoService.getUsuarioLogado();

                Categoria categoria = Categoria.builder()
                                .usuario(usuario)
                                .nome(dados.nome())
                                .cor(dados.cor())
                                .icone(dados.icone())
                                .padrao(false)
                                .ativo(true)
                                .build();

                categoriaRepository.save(categoria);

                return toResponse(categoria);
        }

        public List<CategoriaResponseDTO> listarMinhasCategorias() {

                Usuario usuario = usuarioAutenticadoService.getUsuarioLogado();

                return categoriaRepository
                                .listarDisponiveisParaUsuario(usuario)
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        public CategoriaResponseDTO buscarPorId(Long id) {

                Usuario usuario = usuarioAutenticadoService.getUsuarioLogado();

                Categoria categoria = categoriaRepository.buscarDisponivel(id, usuario)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Categoria não encontrada"));

                return toResponse(categoria);
        }

        public CategoriaResponseDTO atualizar(
                        Long id,
                        CategoriaRequestDTO dados) {

                Usuario usuario = usuarioAutenticadoService.getUsuarioLogado();

                Categoria categoria = categoriaRepository.findByIdAndUsuario(id, usuario)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Categoria não encontrada"));

                if (categoria.getPadrao()) {
                        throw new RuntimeException(
                                        "Categorias padrão não podem ser alteradas");
                }

                categoria.setNome(dados.nome());
                categoria.setCor(dados.cor());
                categoria.setIcone(dados.icone());

                categoriaRepository.save(categoria);

                return toResponse(categoria);
        }

        public void desativar(Long id) {

                Usuario usuario = usuarioAutenticadoService.getUsuarioLogado();

                Categoria categoria = categoriaRepository.findByIdAndUsuario(id, usuario)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Categoria não encontrada"));

                if (categoria.getPadrao()) {
                        throw new RuntimeException(
                                        "Categorias padrão não podem ser removidas");
                }

                categoria.setAtivo(false);

                categoriaRepository.save(categoria);
        }

        private CategoriaResponseDTO toResponse(Categoria categoria) {

                return new CategoriaResponseDTO(
                                categoria.getId(),
                                categoria.getNome(),
                                categoria.getCor(),
                                categoria.getIcone(),
                                categoria.getPadrao(),
                                categoria.getAtivo());
        }

}