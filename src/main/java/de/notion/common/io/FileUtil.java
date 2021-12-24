package de.notion.common.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileUtil {

    public static boolean createDirectory(File path) {
        if (!path.exists()) {
            return path.mkdirs();
        }

        return false;
    }

    public static boolean createDirectory(Path path) {
        try {
            Files.createDirectories(path);
            return true;
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
    }

}
