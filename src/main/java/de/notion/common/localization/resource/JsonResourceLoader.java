package de.notion.common.localization.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            String resName = toResourceName(toBundleName(baseName, locale), "json");
            File file = new File(resourceDirectory, resName);
            if (!file.exists()) {
                try (InputStream in = loader.getResourceAsStream(resName)) {
                    if (in == null) {
                        throw new IllegalArgumentException("Resource file " + resName + " does not exist");
                    }

                    try {
                        Files.copy(in, file.toPath());
                    } catch (IOException e) {
                        return createBundle(resName, in);
                    }
                }
            }

            try (InputStream fileIn = new FileInputStream(file)) {
                return createBundle(resName, fileIn);
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
        JsonElement el = JsonParser.parseReader(new InputStreamReader(source));
        if (!el.isJsonObject()) {
            throw new IllegalArgumentException("JSON resource files must have JSON object root");
        }

        for (Map.Entry<String, JsonElement> e : el.getAsJsonObject().entrySet()) {
            JsonElement value = e.getValue();
            if (value.isJsonArray()) {
                JsonArray array = value.getAsJsonArray();
                String[] res = new String[array.size()];
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