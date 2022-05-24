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

/**
 * This class can capture 3 references of 3 types and set or clear the data using first() and
 * second() and third(). It can be used to return multiple objects of a method, or to easily capture multiple
 * objects without creating their own class.
 *
 * @param <A> the first type, which you want to define
 * @param <B> the second type which you want to define
 * @param <C> the third type which you want to define
 */
public record Triple<A, B, C>(A first, B second, C third) {

    /**
     * Creates a new triple.
     *
     * @param x   first value
     * @param y   second value
     * @param z   third value
     * @param <X> type of first value
     * @param <Y> type of second value
     * @param <Z> type of third value
     * @return the new created triple
     */
    public static <X, Y, Z> Triple<X, Y, Z> of(X x, Y y, Z z) {
        return new Triple<>(x, y, z);
    }

    /**
     * Creates a new empty triple.
     *
     * @param <X> type of first value
     * @param <Y> type of second value
     * @param <Z> type of third value
     * @return the new created triple
     */
    public static <X, Y, Z> Triple<X, Y, Z> empty() {
        return new Triple<>(null, null, null);
    }
}
