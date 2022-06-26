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

import org.jetbrains.annotations.NotNull;

/**
 * This class can capture 2 references of 2 types and set or clear the data using first() and
 * second(). It can be used to return multiple objects of a method, or to easily capture multiple
 * objects without creating their own class.
 *
 * @param <A> the first type, which you want to define
 * @param <B> the second type which you want to define
 */
public final class Pair<A, B> {

    private A first;
    private B second;

    private Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Creates a new pair.
     *
     * @param x   first value
     * @param y   second value
     * @param <X> type of first value
     * @param <Y> type of second value
     * @return the new created pair
     */
    public static <X, Y> @NotNull Pair<X, Y> of(X x, Y y) {
        return new Pair<>(x, y);
    }

    /**
     * Creates a new empty pair.
     *
     * @param <X> type of first value
     * @param <Y> type of second value
     * @return the new created pair
     */
    public static <X, Y> @NotNull Pair<X, Y> empty() {
        return new Pair<>(null, null);
    }

    public A first() {
        return first;
    }

    public Pair<A, B> setFirst(A first) {
        this.first = first;
        return this;
    }

    public B second() {
        return second;
    }

    public Pair<A, B> setSecond(B second) {
        this.second = second;
        return this;
    }
}
