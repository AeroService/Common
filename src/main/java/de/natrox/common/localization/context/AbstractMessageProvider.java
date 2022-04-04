package de.natrox.common.localization.context;

import de.natrox.common.localization.MessageProvider;

import java.util.List;
import java.util.Locale;

public abstract class AbstractMessageProvider<T> implements MessageProvider<T> {

    private final MessageProvider<Locale> delegate;
    private final Contextualizer<T> contextualizer;

    protected AbstractMessageProvider(MessageProvider<Locale> delegate, Contextualizer<T> contextualizer) {
        this.delegate = delegate;
        this.contextualizer = contextualizer;
    }

    @Override
    public String string(T context, String key, Object... params) {
        return delegate.string(contextualizer.locale(context), key, params);
    }

    @Override
    public List<String> stringList(T context, String key, Object... params) {
        return delegate.stringList(contextualizer.locale(context), key, params);
    }

    @Override
    public String[] stringArray(T context, String key, Object... params) {
        return delegate.stringArray(contextualizer.locale(context), key, params);
    }

    public MessageProvider<Locale> delegate() {
        return this.delegate;
    }
}
