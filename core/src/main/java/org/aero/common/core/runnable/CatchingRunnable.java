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

package org.aero.common.core.runnable;

import org.aero.common.core.validate.Check;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Runnable} that prints exceptions thrown.
 *
 * @see Runnable
 */
public record CatchingRunnable(Runnable delegate) implements Runnable {

    /**
     * Constructs a new catching runnable.
     *
     * @param delegate the delegating runnable to run
     */
    public CatchingRunnable(@NotNull final Runnable delegate) {
        Check.notNull(delegate, "delegate");
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            this.delegate.run();
        } catch (final Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
    }
}
