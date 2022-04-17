package ar.com.miura.usersapi.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CustomFileUtils {

    public static String readContent(String path) throws IOException {
        File resource = new ClassPathResource(path).getFile();
        return new String(Files.readAllBytes(resource.toPath()));
    }
}
