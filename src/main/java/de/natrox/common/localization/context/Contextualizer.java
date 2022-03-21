package de.natrox.common.localization.context;

import java.util.Locale;

@FunctionalInterface
public interface Contextualizer<T> {

    Locale locale(T context);

}
