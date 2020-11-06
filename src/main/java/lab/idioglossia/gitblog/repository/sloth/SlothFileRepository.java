package lab.idioglossia.gitblog.repository.sloth;

import lab.idioglossia.gitblog.repository.FileRepository;
import lab.idioglossia.sloth.Collection;
import lab.idioglossia.sloth.SlothStorage;
import lab.idioglossia.sloth.Value;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

public class SlothFileRepository implements FileRepository {
    private final SlothStorage slothStorage;

    public SlothFileRepository(SlothStorage slothStorage) {
        this.slothStorage = slothStorage;
    }

    @SneakyThrows
    @Override
    public void addFile(String col, String fileName, MultipartFile multipartFile) {
        byte[] bytes = multipartFile.getBytes();
        Collection<Object, byte[]> collection = slothStorage.getCollectionOfType(col, Collection.Type.MAP, byte[].class);
        collection.save(fileName, new Value<byte[]>() {
            @Override
            public byte[] getData() {
                return bytes;
            }
        });
    }

    @Override
    public void removeFile(String col, String fileName) {
        slothStorage.getCollectionOfType(col, Collection.Type.MAP, byte[].class).remove(fileName);
    }
}
