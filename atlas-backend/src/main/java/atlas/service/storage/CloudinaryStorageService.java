package atlas.service.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryStorageService implements StorageService {

    private final Cloudinary cloudinary;

    @Override
    public String upload(MultipartFile file) {

        try {

            Map uploadResult = cloudinary.uploader()
                    .upload(
                            file.getBytes(),
                            ObjectUtils.asMap(
                                    "folder", "atlas/perfil"));

            return uploadResult
                    .get("secure_url")
                    .toString();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao enviar imagem");
        }
    }

    @Override
    public void delete(String url) {

        try {

            String publicId = extractPublicId(url);

            cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.emptyMap());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover imagem");
        }
    }

    private String extractPublicId(String url) {

        String caminho = url
                .substring(url.indexOf("/upload/") + 8);

        caminho = caminho.substring(
                caminho.indexOf("/") + 1);

        return caminho.substring(
                0,
                caminho.lastIndexOf("."));
    }
}
