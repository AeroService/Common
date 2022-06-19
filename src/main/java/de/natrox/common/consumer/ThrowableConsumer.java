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

import org.jetbrains.annotations.UnknownNullability;

/**
 * Represents a function that accepts one argument and does not return any value;
 * Function might throw a checked exception instance.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(E)}.
 *
 * @param <E> the type of the first argument to the operation
 * @param <T> the type of the potentially thrown {@link Throwable}
 */
@FunctionalInterface
public interface ThrowableConsumer<E, T extends Throwable> {

    /**
     * Consume the supplied argument, potentially throwing an exception.
     *
     * @param element the input argument
     * @throws T the potentially thrown {@link Throwable}
     */
    void accept(@UnknownNullability E element) throws T;

}
