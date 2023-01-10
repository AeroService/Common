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

package org.aero.common.core.supplier;

import java.util.function.Supplier;

/**
 * Represents a supplier of results. Function might throw a checked {@link Throwable}.
 *
 * <p>There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #get()}.
 *
 * @param <T> the type of results supplied by this supplier
 * @param <U> the type of the potentially thrown {@link Throwable}
 * @see Supplier
 */
@FunctionalInterface
public interface ThrowableSupplier<T, U extends Throwable> {

    /**
     * Gets a result.
     *
     * @return a result
     * @throws U the potentially thrown {@link Throwable}
     */
    T get() throws U;

}
