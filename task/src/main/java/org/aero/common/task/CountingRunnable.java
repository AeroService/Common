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

package org.aero.common.task;

import org.aero.common.core.builder.IBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("MissingJavadocType")
public final class CountingRunnable implements Runnable {

    private final Supplier<Boolean> condition;
    private final Consumer<Boolean> callback;
    private final int step;
    private final AtomicLong count;

    @SuppressWarnings("MissingJavadocMethod")
    private CountingRunnable(@NotNull final Builder builder) {
        this.step = builder.step;
        this.count = new AtomicLong(builder.initial);
        this.condition = builder.condition;
        this.callback = builder.callback;
    }

    @SuppressWarnings("MissingJavadocMethod")
    public static @NotNull Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Override
    public void run() {
        final boolean result = this.condition.get();
        if (result) {
            this.count.getAndAdd(this.step);
            return;
        }
        this.callback.accept(result);
    }

    @SuppressWarnings("MissingJavadocMethod")
    public int step() {
        return this.step;
    }

    @SuppressWarnings("MissingJavadocMethod")
    public long count() {
        return this.count.getAcquire();
    }

    @SuppressWarnings("MissingJavadocMethod")
    public void count(final long count) {
        this.count.set(count);
    }

    public static final class Builder implements IBuilder<CountingRunnable> {

        private int step = 1;
        private long initial = 0;
        private Supplier<Boolean> condition;
        private Consumer<Boolean> callback;

        private Builder() {

        }

        @SuppressWarnings("MissingJavadocMethod")
        public @NotNull Builder step(final int step) {
            this.step = step;
            return this;
        }

        @SuppressWarnings("MissingJavadocMethod")
        public @NotNull Builder initialCount(final long initial) {
            this.initial = initial;
            return this;
        }

        @SuppressWarnings("MissingJavadocMethod")
        public @NotNull Builder condition(final Supplier<Boolean> condition) {
            this.condition = condition;
            return this;
        }

        @SuppressWarnings("MissingJavadocMethod")
        public @NotNull Builder callback(final Consumer<Boolean> callback) {
            this.callback = callback;
            return this;
        }

        @SuppressWarnings("MissingJavadocMethod")
        @Override
        public @NotNull CountingRunnable build() {
            return new CountingRunnable(this);
        }
    }
}
