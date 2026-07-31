package atlas.service;

import atlas.dto.usuario.UsuarioResponseDTO;
import atlas.dto.usuario.AtualizarUsuarioDTO;
import atlas.entity.Usuario;
import atlas.enums.AuthProvider;
import atlas.repository.UsuarioRepository;
import atlas.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import atlas.exception.EmailAlreadyExistsException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UsuarioService {

        private final UsuarioAutenticadoService usuarioAutenticadoService;
        private final UsuarioRepository usuarioRepository;
        private final StorageService storageService;
        private final PasswordEncoder passwordEncoder;

        public UsuarioResponseDTO buscarPerfil() {

                Usuario usuario = usuarioAutenticadoService.getUsuarioLogado();

                return UsuarioResponseDTO.fromEntity(usuario);
        }

        public UsuarioResponseDTO atualizar(
                        AtualizarUsuarioDTO dto) {

                Usuario usuario = usuarioAutenticadoService.getUsuarioLogado();

                if (!usuario.getEmail().equals(dto.email())
                                && usuarioRepository.existsByEmail(dto.email())) {

                        throw new EmailAlreadyExistsException();
                }

                usuario.setNome(dto.nome());
                usuario.setEmail(dto.email());

                Usuario atualizado = usuarioRepository.save(usuario);

                return UsuarioResponseDTO.fromEntity(atualizado);
        }

        public UsuarioResponseDTO atualizarImagemPerfil(MultipartFile file) {

                Usuario usuario = usuarioAutenticadoService.getUsuarioLogado();

                if (file.isEmpty()) {
                        throw new RuntimeException("Arquivo vazio");
                }

                if (!file.getContentType().startsWith("image/")) {
                        throw new RuntimeException("O arquivo precisa ser uma imagem");
                }

                if (usuario.getImagemPerfil() != null) {

                        storageService.delete(
                                        usuario.getImagemPerfil());

                }

                String urlImagem = storageService.upload(file);

                usuario.setImagemPerfil(urlImagem);

                Usuario atualizado = usuarioRepository.save(usuario);

                return UsuarioResponseDTO.fromEntity(atualizado);
        }

        public UsuarioResponseDTO removerImagemPerfil() {

                Usuario usuario = usuarioAutenticadoService.getUsuarioLogado();

                if (usuario.getImagemPerfil() != null) {

                        storageService.delete(
                                        usuario.getImagemPerfil());

                        usuario.setImagemPerfil(null);
                }

                Usuario atualizado = usuarioRepository.save(usuario);

                return UsuarioResponseDTO.fromEntity(atualizado);
        }

        @Transactional
        public void criarSenha(String novaSenha) {

                Usuario usuario = usuarioAutenticadoService.getUsuarioLogado();

                if (usuario.getSenha() != null) {
                        throw new RuntimeException("Usuário já possui uma senha.");
                }

                usuario.setSenha(
                                passwordEncoder.encode(novaSenha));

                if (usuario.getProvider() == AuthProvider.GOOGLE) {
                        usuario.setProvider(AuthProvider.GOOGLE_AND_LOCAL);
                }

                usuarioRepository.save(usuario);
        }

}
