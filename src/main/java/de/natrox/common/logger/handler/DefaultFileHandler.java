package de.natrox.common.logger.handler;

import de.natrox.common.io.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;

public final class DefaultFileHandler extends FileHandler {

    public static final int DEFAULT_COUNT = 8;
    public static final int DEFAULT_LIMIT = 1 << 22;

    private DefaultFileHandler(@NotNull String pattern, int limit, int count, boolean append) throws IOException {
        super(pattern, limit, count, append);
        this.setLevel(Level.ALL);
        this.setEncoding(StandardCharsets.UTF_8.name());
    }

    public static @NotNull DefaultFileHandler newInstance(@NotNull String pattern, boolean append) {
        return DefaultFileHandler.newInstance(pattern, DEFAULT_LIMIT, DEFAULT_COUNT, append);
    }

    public static @NotNull DefaultFileHandler newInstance(@NotNull String pattern, int limit, int count, boolean append) {
        return DefaultFileHandler.newInstance(Path.of(pattern), limit, count, append);
    }

    public static @NotNull DefaultFileHandler newInstance(@NotNull Path pattern, boolean append) {
        return DefaultFileHandler.newInstance(pattern, DEFAULT_LIMIT, DEFAULT_COUNT, append);
    }

    public static @NotNull DefaultFileHandler newInstance(@NotNull Path pattern, int limit, int count, boolean append) {
        try {
            FileUtil.createDirectory(pattern.getParent());
            return new DefaultFileHandler(pattern.toAbsolutePath().toString(), limit, count, append);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to create file handler instance", exception);
        }
    }

    public @NotNull DefaultFileHandler withFormatter(@NotNull Formatter formatter) {
        super.setFormatter(formatter);
        return this;
    }
}
