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

import de.natrox.common.base.Check;
import org.jetbrains.annotations.UnknownNullability;

import java.util.function.Function;

/**
 * Represents a function that accepts four arguments and produces a result. This is the quad-arity specialization of
 * {@link Function}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object, Object, Object, Object)}.
 *
 * @param <T1> the type of the first argument to the function
 * @param <T2> the type of the second argument to the function
 * @param <T3> the type of the third argument to the function
 * @param <R>  the type of the result of the function
 * @see Function
 */
@FunctionalInterface
public interface QuadFunction<T1, T2, T3, T4, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t1 the first function argument
     * @param t2 the second function argument
     * @param t3 the third function argument
     * @param t4 the fourth function argument
     * @return the function result
     */
    R apply(@UnknownNullability T1 t1, @UnknownNullability T2 t2, @UnknownNullability T3 t3, @UnknownNullability T4 t4);

    /**
     * Returns a composed function that first applies this function to its input, and then applies the {@code after}
     * function to the result. If evaluation of either function throws an exception, it is relayed to the caller of the
     * composed function.
     *
     * @param <V>   the type of output of the {@code after} function, and of the composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    default <V> QuadFunction<T1, T2, T3, T4, V> andThen(Function<? super R, ? extends V> after) {
        Check.notNull(after, "after");
        return (T1 t1, T2 t2, T3 t3, T4 t4) -> after.apply(apply(t1, t2, t3, t4));
    }

}
