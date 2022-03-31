package de.natrox.common.localization.context.def;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Supplier;

@SuppressWarnings("ClassCanBeRecord")
public class SystemMessageProvider {

    private final LocaleMessageProvider messageProvider;
    private final Supplier<Locale> contextualizer;

    public SystemMessageProvider(LocaleMessageProvider messageProvider, Supplier<Locale> contextualizer) {
        this.messageProvider = messageProvider;
        this.contextualizer = contextualizer;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String string(String key, Object... params) {
        return messageProvider.string(contextualizer.get(), key, params);
    }

    public List<String> stringList(String key, Object... params) {
        return messageProvider.stringList(contextualizer.get(), key, params);
    }

    public String[] stringArray(String key, Object... params) {
        return messageProvider.stringArray(contextualizer.get(), key, params);
    }

    public static class Builder {

        private final Map<Locale, ResourceBundle> locales;
        private Supplier<Locale> contextualizer;

        public Builder() {
            this.locales = new HashMap<>();
        }

        public Builder contextualizer(Supplier<Locale> contextualizer) {
            this.contextualizer = contextualizer;
            return this;
        }

        public Builder bundle(ResourceBundle bundle) {
            Objects.requireNonNull(bundle);

            locales.put(bundle.getLocale(), bundle);
            return this;
        }

        public SystemMessageProvider build() {
            var builder = LocaleMessageProvider.builder();
            for (var entry : locales.entrySet()) {
                builder.bundle(entry.getValue());
            }

            return new SystemMessageProvider(builder.build(), contextualizer);
        }

    }

}
