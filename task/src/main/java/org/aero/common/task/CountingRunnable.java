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

import org.aero.common.core.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

public final class CountingRunnable implements Runnable {

    private final static int DEFAULT_STEP = 1;
    private final static long DEFAULT_COUNT = 0;

    private final Runnable runnable;
    private final int step;
    private final AtomicLong count;

    public CountingRunnable(@NotNull Runnable runnable, int step, long initialCount) {
        Check.notNull(runnable, "runnable");
        this.runnable = runnable;
        this.step = step;
        this.count = new AtomicLong(initialCount);
    }

    public CountingRunnable(@NotNull Runnable runnable, long initialCount) {
        this(runnable, DEFAULT_STEP, initialCount);
    }

    public CountingRunnable(@NotNull Runnable runnable) {
        this(runnable, DEFAULT_COUNT);
    }

    @Override
    public void run() {
        this.count.getAndAdd(this.step);
        this.runnable.run();
    }

    public int step() {
        return this.step;
    }

    public long count() {
        return this.count.getAcquire();
    }

    public void count(long count) {
        this.count.set(count);
    }
}
