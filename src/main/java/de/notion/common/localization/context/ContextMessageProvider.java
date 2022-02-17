package de.notion.common.localization.context;

import de.notion.common.localization.MessageProvider;

import java.util.List;

public class ContextMessageProvider<T> {

    private final MessageProvider messageProvider;
    private final Contextualizer<T> contextualizer;

    public ContextMessageProvider(MessageProvider messageProvider, Contextualizer<T> contextualizer) {
        this.messageProvider = messageProvider;
        this.contextualizer = contextualizer;
    }

    public String getString(T context, String key, Object... params) {
        return messageProvider.getString(contextualizer.locale(context), key, params);
    }

    public List<String> getStringList(T context, String key, Object... params) {
        return messageProvider.getStringList(contextualizer.locale(context), key, params);
    }

    public String[] getStringArray(T context, String key, Object... params) {
        return messageProvider.getStringArray(contextualizer.locale(context), key, params);
    }

    public MessageProvider delegate() {
        return messageProvider;
    }
}
