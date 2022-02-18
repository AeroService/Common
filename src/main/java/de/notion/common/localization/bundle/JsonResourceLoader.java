package de.notion.common.localization.bundle;

import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonResourceLoader extends ResourceBundle.Control {

    private static final String FORMAT_NAME = "natrox.json";
    private final File resourceDirectory;
    private final Logger logger;

    public JsonResourceLoader(File resourceDirectory) {
        this.resourceDirectory = resourceDirectory;
        this.logger = Logger.getLogger(this.getClass().getSimpleName());

        if (!Files.exists(resourceDirectory.toPath())) {
            try {
                Files.createDirectories(resourceDirectory.toPath());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public List<String> getFormats(String baseName) {
        List<String> list = new ArrayList<>();
        list.add(FORMAT_NAME);
        return list;
    }

    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException {
        if (format.equals(FORMAT_NAME)) {
            var resName = toResourceName(toBundleName(baseName, locale), "json");
            var file = new File(resourceDirectory, resName);
            if (!file.exists()) {
                try (var in = loader.getResourceAsStream(resName)) {
                    try {
                        if (in != null) {
                            Files.copy(in, file.toPath());
                            return newBundle(baseName, locale, format, loader, reload);
                        }
                    } catch (IOException e) {
                        return createBundle(resName, in);
                    }
                }
            } else {
                try (var fileIn = new FileInputStream(file)) {
                    return createBundle(resName, fileIn);
                }
            }
        }
        return null;
    }

    @Override
    public long getTimeToLive(String baseName, Locale locale) {
        return TTL_NO_EXPIRATION_CONTROL;
    }

    private ResourceBundle createBundle(String resourceName, InputStream source) {
        Map<String, Object> entries = new HashMap<>();
        var el = JsonParser.parseReader(new InputStreamReader(source, StandardCharsets.UTF_8));
        if (!el.isJsonObject()) {
            throw new IllegalArgumentException("JSON resource files must have JSON object root");
        }

        for (var e : el.getAsJsonObject().entrySet()) {
            var value = e.getValue();
            if (value.isJsonArray()) {
                var array = value.getAsJsonArray();
                var res = new String[array.size()];
                for (int i = 0; i < res.length; i++) {
                    res[i] = array.get(i).getAsString();
                }
                entries.put(e.getKey(), res);
            } else if (value.isJsonObject()) {
                logger.log(Level.WARNING, "Invalid value for key {0} in resource {1}",
                        new Object[]{e.getKey(), resourceName});
            } else {
                entries.put(e.getKey(), e.getValue().getAsString());
            }
        }

        return new MapResourceBundle(entries);
    }

}