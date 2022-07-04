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

package de.natrox.common.function;

import java.util.function.Function;

/**
 * Represents a function that accepts one argument and produces a result with the same type as the input.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object)}.
 *
 * @param <T> the type of the input to the function and the output of the function
 * @see Function
 */
@FunctionalInterface
public interface SingleTypeFunction<T> extends Function<T, T> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the first function argument
     * @return the function result
     */
    @Override
    T apply(T t);

}
