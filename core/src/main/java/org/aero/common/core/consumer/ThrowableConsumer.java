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

package org.aero.common.core.consumer;

import org.aero.common.core.validate.Check;

/**
 * Represents an operation that accepts one input argument and returns no result. Unlike most other functional
 * interfaces, {@code Consumer} is expected to operate via side-effects. Function might throw a checked
 * {@link Throwable}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(T)}.
 *
 * @param <T> the type of the argument to the operation
 * @param <U> the type of the potentially thrown {@link Throwable}
 */
@FunctionalInterface
public interface ThrowableConsumer<T, U extends Throwable> {

    /**
     * Performs this operation on the given argument, potentially throwing an exception.
     *
     * @param t the input argument
     * @throws U the potentially thrown {@link Throwable}
     */
    void accept(T t) throws U;

    /**
     * Returns a composed {@code ThrowableConsumer} that performs, in sequence, this operation followed by the
     * {@code after} operation. If performing either operation throws an exception, it is relayed to the caller of the
     * composed operation. If performing this operation throws an exception, the {@code after} operation will not be
     * performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code ThrowableConsumer} that performs in sequence this operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default ThrowableConsumer<T, U> andThen(final ThrowableConsumer<? super T, ? extends U> after) {
        Check.notNull(after, "after");
        return (t) -> {
            this.accept(t);
            after.accept(t);
        };
    }

}
