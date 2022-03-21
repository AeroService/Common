package de.natrox.common.localization.bundle;

import org.jetbrains.annotations.NotNull;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

public class MapResourceBundle extends ResourceBundle {

    private final Map<String, Object> resources;

    public MapResourceBundle(Map<String, Object> resources) {
        this.resources = new HashMap<>(resources);
    }

    @Override
    protected Object handleGetObject(@NotNull String key) {
        return resources.get(key);
    }

    @NotNull
    @Override
    public Enumeration<String> getKeys() {
        return new Vector<>(resources.keySet()).elements();
    }
}