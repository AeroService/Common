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

package de.natrox.common.supplier;

import org.jetbrains.annotations.UnknownNullability;

/**
 * Represents a function that accepts zero arguments and returns some value.
 * Function might throw a checked exception instance.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #get()}.
 *
 * @param <O> the type of argument supplied
 * @param <T> the type of the potentially thrown {@link Throwable}
 */
@FunctionalInterface
public interface ThrowableSupplier<O, T extends Throwable> {

    /**
     * Returns a result, potentially throwing an exception.
     *
     * @return a result
     * @throws T the potentially thrown {@link Throwable}
     */
    @UnknownNullability O get() throws T;

}
