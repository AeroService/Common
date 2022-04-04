package de.natrox.common.localization.context;

import de.natrox.common.localization.MessageProvider;

import java.util.Locale;
import java.util.UUID;

@SuppressWarnings("ClassCanBeRecord")
public class UUIDMessageProvider extends AbstractMessageProvider<UUID> {

    private UUIDMessageProvider(MessageProvider<Locale> localeMessageProvider, Contextualizer<UUID> contextualizer) {
        super(localeMessageProvider, contextualizer);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractBuilder<UUID, Builder> {

        @Override
        public UUIDMessageProvider build() {
            var builder = LocaleMessageProvider.builder();
            for (var entry : locales.entrySet()) {
                builder.bundle(entry.getValue());
            }

            var localeMessageProvider = builder.build();
            return new UUIDMessageProvider(localeMessageProvider, this.contextualizer);
        }
    }
}
