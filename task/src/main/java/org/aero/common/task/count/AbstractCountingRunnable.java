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

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("MissingJavadocType")
public abstract class AbstractCountingRunnable implements CountingRunnable {

    protected final Supplier<Boolean> condition;
    protected final Runnable callback;
    protected final int step;
    protected final AtomicLong count;

    @SuppressWarnings("MissingJavadocMethod")
    public AbstractCountingRunnable(final int step, final long count, final Supplier<Boolean> condition, final Runnable callback) {
        this.step = step;
        this.count = new AtomicLong(count);
        this.condition = condition;
        this.callback = callback;
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Override
    public void run() {
        if (this.condition.get()) {
            this.count.getAndAdd(this.step);
        }
        this.callback.run();
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

}
