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

package de.natrox.common.function;

import org.jetbrains.annotations.UnknownNullability;

/**
 * Represents a function that accepts one argument and returns a value;
 * Function might throw a checked exception instance.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(I)}.
 *
 * @param <I> the type of the first argument to the function
 * @param <O> the type of the result of the function
 * @param <T> the type of the thrown checked exception
 */
@FunctionalInterface
public interface ThrowableFunction<I, O, T extends Throwable> {

    /**
     * Returns a result and consume the supplied argument, potentially throwing an exception.
     *
     * @param i the function argument
     * @return the function result
     * @throws T the potentially thrown {@link Throwable}
     */
    @UnknownNullability O apply(@UnknownNullability I i) throws T;

}
