/*
 * Copyright 2020-2022 NatroxMC
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

package de.natrox.serialize;

import io.leangen.geantyref.GenericTypeReflector;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.function.Predicate;

public interface Registrable<T, U> {

    static <T, U> @NotNull Registrable<T, U> create() {
        return new RegistrableImpl<>();
    }

    @NotNull Registrable<T, U> type(@NotNull Predicate<Type> test);

    default @NotNull Registrable<T, U> type(@NotNull Type type) {
        return this.type(test -> this.testType(test, type));
    }

    default @NotNull Registrable<T, U> type(@NotNull Class<T> type) {
        return this.type((Type) type);
    }

    default @NotNull Registrable<T, U> type(@NotNull TypeToken<T> type) {
        return this.type(type.getType());
    }

    default @NotNull Registrable<T, U> typeExact(@NotNull Type type) {
        return this.type(test -> test.equals(type));
    }

    default @NotNull Registrable<T, U> typeExact(@NotNull Class<T> type) {
        return this.typeExact((Type) type);
    }

    default @NotNull Registrable<T, U> typeExact(@NotNull TypeToken<T> type) {
        return this.typeExact(type.getType());
    }

    @NotNull Registrable<T, U> inputType(@NotNull Predicate<Type> test);

    default @NotNull Registrable<T, U> inputType(@NotNull Type type) {
        return this.inputType(test -> this.testType(test, type));
    }

    default @NotNull Registrable<T, U> inputType(@NotNull Class<U> type) {
        return this.inputType((Type) type);
    }

    default @NotNull Registrable<T, U> inputType(@NotNull TypeToken<U> type) {
        return this.inputType(type.getType());
    }

    default @NotNull Registrable<T, U> inputTypeExact(@NotNull Type type) {
        return this.inputType(test -> test.equals(type));
    }

    default @NotNull Registrable<T, U> inputTypeExact(@NotNull Class<U> type) {
        return this.inputType((Type) type);
    }

    default @NotNull Registrable<T, U> inputTypeExact(@NotNull TypeToken<U> type) {
        return this.inputType(type.getType());
    }

    private boolean testType(Type test, Type type) {
        if (GenericTypeReflector.isSuperType(type, test)) {
            return true;
        }

        if (test instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) test).getUpperBounds();
            if (upperBounds.length == 1) {
                return GenericTypeReflector.isSuperType(type, upperBounds[0]);
            }
        }
        return false;
    }
}
