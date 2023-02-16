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

package org.aero.common.task.count;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("MissingJavadocType")
final class CountingRunnableImpl extends AbstractCountingRunnable {

    private CountingRunnableImpl(@NotNull final CountingRunnableImpl.BuilderImpl builder) {
        super(builder.step, builder.initial, builder.condition, builder.callback);
    }

    static final class BuilderImpl implements CountingRunnable.Builder {

        private int step = 1;
        private long initial = 0;
        private Supplier<Boolean> condition;
        private Consumer<Boolean> callback;

        BuilderImpl() {

        }

        @SuppressWarnings("MissingJavadocMethod")
        public @NotNull CountingRunnableImpl.BuilderImpl step(final int step) {
            this.step = step;
            return this;
        }

        @SuppressWarnings("MissingJavadocMethod")
        public @NotNull CountingRunnableImpl.BuilderImpl initialCount(final long initial) {
            this.initial = initial;
            return this;
        }

        @SuppressWarnings("MissingJavadocMethod")
        public @NotNull CountingRunnableImpl.BuilderImpl condition(final Supplier<Boolean> condition) {
            this.condition = condition;
            return this;
        }

        @SuppressWarnings("MissingJavadocMethod")
        public @NotNull CountingRunnableImpl.BuilderImpl callback(final Consumer<Boolean> callback) {
            this.callback = callback;
            return this;
        }

        @SuppressWarnings("MissingJavadocMethod")
        @Override
        public @NotNull CountingRunnableImpl build() {
            return new CountingRunnableImpl(this);
        }
    }
}
