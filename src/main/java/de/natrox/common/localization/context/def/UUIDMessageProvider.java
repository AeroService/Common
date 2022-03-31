package de.natrox.common.localization.context.def;

import de.natrox.common.localization.MessageProvider;
import de.natrox.common.localization.context.Contextualizer;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@SuppressWarnings("ClassCanBeRecord")
public class UUIDMessageProvider implements MessageProvider<UUID> {

    private final MessageProvider<Locale> localeMessageProvider;
    private final Contextualizer<UUID> contextualizer;

    private UUIDMessageProvider(MessageProvider<Locale> localeMessageProvider, Contextualizer<UUID> contextualizer) {
        this.localeMessageProvider = localeMessageProvider;
        this.contextualizer = contextualizer;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String string(UUID context, String key, Object... params) {
        return localeMessageProvider.string(contextualizer.locale(context), key, params);
    }

    @Override
    public List<String> stringList(UUID context, String key, Object... params) {
        return localeMessageProvider.stringList(contextualizer.locale(context), key, params);
    }

    @Override
    public String[] stringArray(UUID context, String key, Object... params) {
        return localeMessageProvider.stringArray(contextualizer.locale(context), key, params);
    }

    public MessageProvider<Locale> delegate() {
        return this.localeMessageProvider;
    }

    public static class Builder extends AbstractBuilder<UUID, Builder> {

        @Override
        public UUIDMessageProvider build() {
            var builder = LocaleMessageProvider
                    .builder()
                    .contextualizer(context -> context);
            for (var entry : locales.entrySet()) {
                builder.bundle(entry.getValue());
            }

            var localeMessageProvider = builder.build();
            return new UUIDMessageProvider(localeMessageProvider, this.contextualizer);
        }
    }
}
