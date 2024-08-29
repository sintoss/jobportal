package com.example.jobportal.util;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class FileUploadUtil {


    public static String getFileName(MultipartFile file) {

        String fileName = "";

        if (!Objects.equals(file.getOriginalFilename(), "")) {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        }

        return fileName;
    }

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path path = uploadPath.resolve(fileName);
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Could not save file " + fileName, e);
        }
    }

}
