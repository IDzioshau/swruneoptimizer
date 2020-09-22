package by.dobrush.swruneoptimizer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class FileService {

    private static File getFile(String filePath) {
        return FileUtils.getFile(filePath);
    }

    @SneakyThrows
    public static void writeToFile(String filePath, Object object) {
        File file = new File(filePath);
        file.createNewFile();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, object);
    }

    public static String readFile(String filePath) {
        File file = getFile(filePath);
        return readFile(file);
    }

    @SneakyThrows
    private static String readFile(File file) {
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }
}
