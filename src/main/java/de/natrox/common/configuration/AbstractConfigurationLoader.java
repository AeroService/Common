package de.natrox.common.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.natrox.common.io.FileUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public abstract class AbstractConfigurationLoader<T> {

    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Path directory;
    private final Class<? extends T> type;

    public AbstractConfigurationLoader(Path directory, Class<? extends T> type) {
        this.directory = directory;
        this.type = type;
    }

    public T loadFile() {
        return loadFromJson(directory, type);
    }

    public T createFile() {
        var parent = directory.getParent();

        if (!existsFile()) {
            if (parent != null && !Files.exists(parent)) {
                FileUtil.createDirectory(parent.toFile());
            }

            T config = createConfig();
            saveConfig(config);
            return config;
        }

        return null;
    }

    public T createConfig() {
        return createObject();
    }

    public boolean deleteFile() {
        try {
            return Files.deleteIfExists(directory);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public abstract T createObject();

    public T loadOrCreateFile() {
        if (!existsFile()) {
            return createFile();
        }

        return loadFile();
    }

    public T loadOrCreateConfig() {
        if (!existsFile()) {
            return createConfig();
        }

        return loadFile();
    }

    public void loadOrCreateFile(Consumer<T> createConsumer, Consumer<T> loadConsumer) {
        if (!existsFile()) {
            createConsumer.accept(createFile());
        }

        loadConsumer.accept(loadFile());
    }

    public void loadOrCreateConfig(Consumer<T> createConsumer, Consumer<T> loadConsumer) {
        if (!existsFile()) {
            createConsumer.accept(createConfig());
        }

        loadConsumer.accept(loadFile());
    }

    public boolean existsFile() {
        return Files.exists(directory);
    }

    public void saveConfig(T config) {
        saveToJson(config, directory);
    }

    private void saveToJson(Object object, Path externalPath) {
        if (object == null) {
            throw new IllegalArgumentException("The provided object can not be null.");
        }

        if (externalPath == null) {
            throw new IllegalArgumentException("The provided path can not be null.");
        }

        try (var writer = new FileWriter(externalPath.toFile())) {
            writer.write(GSON.toJson(object));
        } catch (IOException ignored) {
        }
    }

    private T loadFromJson(Path source, Class<? extends T> objectClass) {
        if (source == null) {
            throw new IllegalArgumentException("The provided path can not be null.");
        }

        if (objectClass == null) {
            throw new IllegalArgumentException("The provided objectclass can not be null.");
        }

        try (var reader = new BufferedReader(new FileReader(source.toFile()))) {
            return GSON.fromJson(reader, objectClass);
        } catch (IOException e) {
            return null;
        }
    }

}