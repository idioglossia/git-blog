package lab.idioglossia.gitblog.repository;

import org.springframework.web.multipart.MultipartFile;

public interface FileRepository {
    void addFile(String col, String fileName, MultipartFile multipartFile);
    void removeFile(String col, String fileName);
}
