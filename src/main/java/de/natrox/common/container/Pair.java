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

package de.natrox.common.container;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a class that can capture two references of two types and set or clear the data using
 * {@link #setFirst(Object)} and {@link #setSecond(Object)}. It can be used to return multiple objects of a method, or
 * to easily capture multiple objects without creating their own class.
 *
 * @param <T> the type of the first reference
 * @param <U> the type of the second reference
 */
public final class Pair<T, U> {

    private T first;
    private U second;

    private Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Creates a new pair.
     *
     * @param first  the first reference
     * @param second the second reference
     * @param <T>    the type of the first reference
     * @param <U>    the type of the second reference
     * @return the created pair
     */
    public static <T, U> @NotNull Pair<T, U> of(T first, U second) {
        return new Pair<>(first, second);
    }

    /**
     * Creates a new empty pair.
     *
     * @param <T> the type of the first reference
     * @param <U> the type of the second reference
     * @return the new created pair
     */
    public static <T, U> @NotNull Pair<T, U> empty() {
        return new Pair<>(null, null);
    }

    /**
     * Returns the first reference.
     *
     * @return the first reference
     */
    public T first() {
        return first;
    }

    /**
     * Sets the first reference.
     *
     * @param first the first reference
     * @return this pair, for chaining
     */
    public Pair<T, U> setFirst(T first) {
        this.first = first;
        return this;
    }

    /**
     * Returns the second reference.
     *
     * @return the second reference
     */
    public U second() {
        return second;
    }

    /**
     * Sets the second reference.
     *
     * @param second the second reference
     * @return this pair, for chaining
     */
    public Pair<T, U> setSecond(U second) {
        this.second = second;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }

        Pair<?, ?> that = (Pair<?, ?>) obj;
        return Objects.deepEquals(this.first(), that.first())
            && Objects.deepEquals(this.second(), that.second());
    }
}
