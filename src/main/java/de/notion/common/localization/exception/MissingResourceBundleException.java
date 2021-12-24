package de.notion.common.localization.exception;

import java.util.Locale;

public class MissingResourceBundleException extends RuntimeException {

    private final Locale locale;

    public MissingResourceBundleException(Locale locale) {
        super("Can't find a resource bundle for locale: " + locale.getDisplayName());

        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}