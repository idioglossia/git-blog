package lab.idioglossia.gitblog.service.panel;

import lab.idioglossia.gitblog.repository.FileRepository;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@Profile("initialized")
public class FileService {
    private final FileRepository fileRepository;
    private final Tika tika = new Tika();

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void removeImage(String filename){
        fileRepository.removeFile("images", filename);
    }

    public String createImage(MultipartFile multipartFile){
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        String filename = UUID.randomUUID().toString() + "." + extension;
        createImage(multipartFile, filename);
        return filename;
    }

    public void createImage(MultipartFile multipartFile, String filename){
        validateImage(multipartFile);
        fileRepository.addFile("images", filename, multipartFile);
    }

    @SneakyThrows
    private void validateImage(MultipartFile multipartFile){
        String mime = tika.detect(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
        if(mime.contains("/") && mime.split("/")[0].equalsIgnoreCase("image"))
            return;
        throw new RuntimeException("File is not an image!");
    }
}
