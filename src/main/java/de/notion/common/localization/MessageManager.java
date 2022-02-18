package de.notion.common.localization;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageManager implements MessageProvider {

    private final Map<Locale, ResourceBundle> bundles;

    public MessageManager(Map<Locale, ResourceBundle> bundles) {
        this.bundles = bundles;
    }

    @Override
    public String getString(Locale locale, String key, Object... params) {
        var bundle = getBundle(locale);
        var bundleString = bundle.getString(key);
        var messageFormat = new MessageFormat(bundleString, locale);

        return messageFormat.format(params);
    }

    @Override
    public String[] getStringArray(Locale locale, String key, Object... params) {
        var bundle = getBundle(locale);
        var bundleArray = bundle.getStringArray(key);
        var localizedArray = new String[bundleArray.length];

        for (int i = 0; i < bundleArray.length; i++) {
            var line = bundleArray[i];
            var messageFormat = new MessageFormat(line, locale);
            localizedArray[i] = messageFormat.format(params);
        }

        return localizedArray;
    }

    @Override
    public List<String> getStringList(Locale locale, String key, Object... params) {
        return Arrays.asList(getStringArray(locale, key, params));
    }

    private ResourceBundle getBundle(Locale locale) {
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
}
