/*
 * Copyright 2020-2022 NatroxMC team
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

package de.natrox.common.validate;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * Convenient class to check for common exceptions.
 */
public final class Check {

    private Check() {
        throw new UnsupportedOperationException();
    }

    @Contract("null, _ -> fail")
    public static void notNull(@Nullable Object object, @NotNull String reason) {
        if (Objects.isNull(object)) {
            throw new NullPointerException(reason);
        }
    }

    @Contract("null, _, _ -> fail")
    public static void notNull(@Nullable Object object, @NotNull String reason, Object... arguments) {
        if (Objects.isNull(object)) {
            throw new NullPointerException(MessageFormat.format(reason, arguments));
        }
    }

    @Contract("true, _ -> fail")
    public static void argCondition(boolean condition, @NotNull String reason) {
        if (condition) {
            throw new IllegalArgumentException(reason);
        }
    }

    @Contract("true, _, _ -> fail")
    public static void argCondition(boolean condition, @NotNull String reason, Object... arguments) {
        if (condition) {
            throw new IllegalArgumentException(MessageFormat.format(reason, arguments));
        }
    }

    @Contract("_ -> fail")
    public static void fail(@NotNull String reason) {
        throw new IllegalArgumentException(reason);
    }

    @Contract("true, _ -> fail")
    public static void stateCondition(boolean condition, @NotNull String reason) {
        if (condition) {
            throw new IllegalStateException(reason);
        }
    }

    @Contract("true, _, _ -> fail")
    public static void stateCondition(boolean condition, @NotNull String reason, Object... arguments) {
        if (condition) {
            throw new IllegalStateException(MessageFormat.format(reason, arguments));
        }
    }

}
