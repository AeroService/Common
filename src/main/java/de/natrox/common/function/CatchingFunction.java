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

import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Function that prints exceptions thrown
 */
public record CatchingFunction<T, R>(Function<T, R> delegate) implements Function<T, R> {

    public CatchingFunction(@NotNull Function<T, R> delegate) {
        Check.notNull(delegate, "delegate");
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public R apply(T t) {
        try {
            return delegate.apply(t);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }
}
