/*
 * Copyright 2020-2023 AeroService
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.aero.common.core.validate;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * Represents a convenient class to check for common exceptions.
 */
public final class Check {

    private Check() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Contract("null, _ -> fail")
    public static void notNull(final Object object, final @NotNull String reason) {
        if (!Objects.isNull(object)) {
            return;
        }
        throw new IllegalArgumentException(reason);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Contract("null, _, _ -> fail")
    public static void notNull(final Object object, @NotNull final String reason, final Object... arguments) {
        if (!Objects.isNull(object)) {
            return;
        }
        throw new IllegalArgumentException(MessageFormat.format(reason, arguments));
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Contract("true, _ -> fail")
    public static void argCondition(final boolean condition, @NotNull final String reason) {
        if (!condition) {
            return;
        }
        throw new IllegalArgumentException(reason);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Contract("true, _, _ -> fail")
    public static void argCondition(final boolean condition, @NotNull final String reason, final Object... arguments) {
        if (!condition) {
            return;
        }
        throw new IllegalArgumentException(MessageFormat.format(reason, arguments));
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Contract("_ -> fail")
    public static void fail(@NotNull final String reason) {
        throw new IllegalArgumentException(reason);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Contract("true, _ -> fail")
    public static void stateCondition(final boolean condition, @NotNull final String reason) {
        if (!condition) {
            return;
        }
        throw new IllegalStateException(reason);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Contract("true, _, _ -> fail")
    public static void stateCondition(final boolean condition, @NotNull final String reason, final Object... arguments) {
        if (!condition) {
            return;
        }
        throw new IllegalStateException(MessageFormat.format(reason, arguments));
    }
}
