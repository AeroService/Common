package de.natrox.common.localization.context;

import de.natrox.common.localization.MessageProvider;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@SuppressWarnings("ClassCanBeRecord")
public class LocaleMessageProvider implements MessageProvider<Locale> {

    private final Map<Locale, ResourceBundle> bundles;
    private final Contextualizer<Locale> contextualizer;

    private LocaleMessageProvider(Map<Locale, ResourceBundle> bundles, Contextualizer<Locale> contextualizer) {
        this.bundles = bundles;
        this.contextualizer = contextualizer;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String string(Locale locale, String key, Object... params) {
        var context = contextualizer.locale(locale);
        var bundle = bundle(context);
        var bundleString = bundle.getString(key);
        var messageFormat = new MessageFormat(bundleString, context);

        return messageFormat.format(params);
    }

    @Override
    public String[] stringArray(Locale locale, String key, Object... params) {
        var context = contextualizer.locale(locale);
        var bundle = bundle(context);
        var bundleArray = bundle.getStringArray(key);
        var localizedArray = new String[bundleArray.length];

        for (int i = 0; i < bundleArray.length; i++) {
            var line = bundleArray[i];
            var messageFormat = new MessageFormat(line, context);
            localizedArray[i] = messageFormat.format(params);
        }

        return localizedArray;
    }

    private ResourceBundle bundle(Locale locale) {
        var bundle = bundles.get(locale);

        if (bundle == null) {
            throw new MissingResourceException(
                    "Can't find resource bundle, locale " + locale.getLanguage(),
                    ResourceBundle.class.getName(),
                    locale.getLanguage()
            );
        }
        return bundle;
    }

    public static class Builder extends AbstractBuilder<Locale, Builder> {

        public Builder() {
            this.contextualizer = context -> context;
        }

        @Override
        public LocaleMessageProvider build() {
            return new LocaleMessageProvider(this.locales, this.contextualizer);
        }
    }
}
