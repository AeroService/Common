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

package org.aero.common.core.function;

import org.aero.common.core.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Represents a {@link Function} that prints exceptions thrown.
 *
 * @param <T> the type of the first argument to the operation
 * @see Function
 */
public record CatchingFunction<T, R>(Function<T, R> delegate) implements Function<T, R> {

    /**
     * Constructs a new catching function.
     *
     * @param delegate the delegating runnable to run
     */
    public CatchingFunction(@NotNull final Function<T, R> delegate) {
        Check.notNull(delegate, "delegate");
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public R apply(final T t) {
        try {
            return this.delegate.apply(t);
        } catch (final Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
    }
}
