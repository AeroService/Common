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

import org.aero.common.core.builder.IBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("MissingJavadocType")
public interface CountingRunnable extends Runnable {

    @SuppressWarnings("MissingJavadocMethod")
    static @NotNull Builder builder() {
        return new CountingRunnableImpl.BuilderImpl();
    }

    @SuppressWarnings("MissingJavadocMethod")
    long count();

    @SuppressWarnings("MissingJavadocMethod")
    void count(long count);

    @SuppressWarnings("MissingJavadocType")
    interface Builder extends IBuilder<CountingRunnable> {

        @SuppressWarnings("MissingJavadocMethod")
        @NotNull Builder step(int step);

        @SuppressWarnings("MissingJavadocMethod")
        @NotNull Builder initialCount(long initial);

        @SuppressWarnings("MissingJavadocMethod")
        @NotNull Builder condition(Supplier<Boolean> condition);

        @SuppressWarnings("MissingJavadocMethod")
        @NotNull Builder callback(Runnable callback);

    }
}
