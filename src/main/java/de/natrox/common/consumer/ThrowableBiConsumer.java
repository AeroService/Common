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

package de.natrox.common.consumer;

import de.natrox.common.validate.Check;

/**
 * Represents an operation that accepts two input arguments and returns no
 * result. Unlike most other functional interfaces, {@code Consumer} is expected
 * to operate via side-effects. Function might throw a checked {@link Throwable}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(T, U)}.
 *
 * @param <T> the type of the first argument to the operation
 * @param <T> the type of the second argument to the operation
 * @param <V> the type of the potentially thrown {@link Throwable}
 */
@FunctionalInterface
public interface ThrowableBiConsumer<T, U, V extends Throwable> {

    /**
     * Performs this operation on the given arguments, potentially throwing an exception.
     *
     * @param t the fist input argument
     * @param u the second input argument
     * @throws V the potentially thrown {@link Throwable}
     */
    void accept(T t, U u) throws V;

    /**
     * Returns a composed {@code ThrowableBiConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation. If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code ThrowableBiConsumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default ThrowableBiConsumer<T, U, V> andThen(ThrowableBiConsumer<? super T, ? super U, ? extends V> after) {
        Check.notNull(after, "after");
        return (t, u) -> {
            this.accept(t, u);
            after.accept(t, u);
        };
    }

}
