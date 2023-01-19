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

package org.aero.common.core.container;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a class that can capture three references of three types and set or clear the data using
 * {@link #first(Object)} (Object)}, {@link #second(Object)} (Object)} and {@link #third(Object)} (Object)}. It can be used to return
 * multiple objects of a method, or to easily capture multiple objects without creating their own class.
 *
 * @param <T> the type of the first reference
 * @param <U> the type of the second reference
 * @param <V> the type of the third reference
 */
public final class Triple<T, U, V> {

    private T first;
    private U second;
    private V third;

    private Triple(final T first, final U second, final V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * Creates a new triple.
     *
     * @param first  the first reference
     * @param second the second reference
     * @param third  the third reference
     * @param <T>    the type of the first reference
     * @param <U>    the type of the second reference
     * @param <V>    the type of the third reference
     * @return the created triple
     */
    public static <T, U, V> @NotNull Triple<T, U, V> of(final T first, final U second, final V third) {
        return new Triple<>(first, second, third);
    }

    /**
     * Creates a new empty triple.
     *
     * @param <T> the type of the first reference
     * @param <U> the type of the second reference
     * @param <V> the type of the third reference
     * @return the created triple
     */
    public static <T, U, V> @NotNull Triple<T, U, V> empty() {
        return new Triple<>(null, null, null);
    }

    /**
     * Returns the first reference.
     *
     * @return the first reference
     */
    public T first() {
        return this.first;
    }

    /**
     * Sets the first reference.
     *
     * @param first the first reference
     * @return this pair, for chaining
     */
    public Triple<T, U, V> first(final T first) {
        this.first = first;
        return this;
    }

    /**
     * Returns the second reference.
     *
     * @return the second reference
     */
    public U second() {
        return this.second;
    }

    /**
     * Sets the second reference.
     *
     * @param second the second reference
     * @return this pair, for chaining
     */
    public Triple<T, U, V> second(final U second) {
        this.second = second;
        return this;
    }

    /**
     * Returns the third reference.
     *
     * @return the third reference
     */
    public V third() {
        return this.third;
    }

    /**
     * Sets the third reference.
     *
     * @param third the third reference
     * @return this pair, for chaining
     */
    public Triple<T, U, V> third(final V third) {
        this.third = third;
        return this;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof final Triple<?, ?, ?> that)) {
            return false;
        }

        return Objects.deepEquals(this.first(), that.first())
            && Objects.deepEquals(this.second(), that.second())
            && Objects.deepEquals(this.third(), that.third());
    }
}
