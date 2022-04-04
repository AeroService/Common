package de.natrox.common.localization;

import de.natrox.common.localization.context.Contextualizer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Provides localized chat messages and other text displayed.
 * <p>
 * These texts are uniquely identified by a key. To prevent conflicts between different services using this API,
 * all keys should be prepended with an identifier for the component it is used in and a dot as a separator, e.g.:
 * <pre>
 * lobby.navigator_title
 * bedwars.bed_broken
 * </pre>
 * <p>
 * For each available {@link Locale}, all keys should be associated with the corresponding translation, which is a
 * {@link String} or string array value. Translations may contain format elements as defined by
 * {@link java.text.MessageFormat}.
 */
public interface MessageProvider<T> {

    /**
     * Returns the translated text for the specified key, with its variables (if any) replaced with the given parameters
     * (if provided) in a localized format.
     * <p>
     * Texts defined as string arrays in the underlying mapping will be concatenated, with the individual strings split
     * by a newline (\n).
     *
     * @param context The context T.
     * @param key     The unique identifier of the text.
     * @param params  The parameters to replace the text's variables with.
     * @return The text associated with the key, translated and formatted for the specified {@link Locale}.
     * @throws IllegalArgumentException If the specified {@link Locale} is null or there is no translation mapping available for it.
     * @throws IllegalArgumentException If the key is null.
     */
    String string(T context, String key, Object... params);

    /**
     * Returns the translated text, consisting of one or multiple strings, for the specified key, with its variables
     * (if any) replaced with the given parameters (if provided) in a localized format. Parameters are applied to all
     * individual strings.
     * <p>
     * For texts defined as single strings in the underlying mapping, the returned array will have one element.
     *
     * @param context The context T.
     * @param key     The unique identifier of the text.
     * @param params  The parameters to replace the text's variables with.
     * @return An array of strings associated with the key, translated and formatted for the specified {@link Locale}.
     * @throws IllegalArgumentException If the specified {@link Locale} is null or there is no translation mapping available for it.
     * @throws IllegalArgumentException If the key is null.
     */
    String[] stringArray(T context, String key, Object... params);

    /**
     * Returns the translated text, consisting of one or multiple strings, for the specified key, with its variables
     * (if any) replaced with the given parameters (if provided) in a localized format. Parameters are applied to all
     * individual strings.
     * <p>
     * For texts defined as single strings in the underlying mapping, the returned {@link List} will have one element.
     *
     * @param context The context T.
     * @param key     The unique identifier of the text.
     * @param params  The parameters to replace the text's variables with.
     * @return A {@link List} of strings associated with the key, translated and formatted for the specified {@link Locale}.
     * @throws IllegalArgumentException If the specified {@link Locale} is null or there is no translation mapping available for it.
     * @throws IllegalArgumentException If the key is null.
     */
    default List<String> stringList(T context, String key, Object... params) {
        return Arrays.asList(stringArray(context, key, params));
    }

    abstract class AbstractBuilder<T, U extends AbstractBuilder<T, U>> {

        protected final Map<Locale, ResourceBundle> locales;
        protected Contextualizer<T> contextualizer;

        public AbstractBuilder() {
            this.locales = new HashMap<>();
        }

        public U contextualizer(Contextualizer<T> contextualizer) {
            this.contextualizer = contextualizer;
            return (U) this;
        }

        public U bundle(ResourceBundle bundle) {
            Objects.requireNonNull(bundle);

            locales.put(bundle.getLocale(), bundle);
            return (U) this;
        }

        public abstract MessageProvider<T> build();

    }

}
