/*
 * Copyright 2020-2022 NatroxMC
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.natrox.common.task;

import de.natrox.common.validate.Check;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public final class TaskImpl {

    private TaskImpl() {
        throw new UnsupportedOperationException();
    }

    public abstract static non-sealed class AbstractTask implements Task {

        public abstract void run();

    }

    public abstract static class AbstractFutureTask extends AbstractTask {

        private Future<?> future;

        protected abstract Future<?> runFuture();

        @Override
        public boolean isCancelled() {
            return this.future != null && this.future.isCancelled();
        }

        @Override
        public boolean isDone() {
            return this.future != null && this.future.isDone();
        }

        @Override
        public void run() {
            this.future = this.runFuture();
        }

        @Override
        public void cancel() {
            if (this.future == null) {
                return;
            }

            this.future.cancel(false);
        }
    }

    public static class FutureTaskImpl extends AbstractFutureTask {

        private final Supplier<Future<?>> supplier;

        public FutureTaskImpl(@NotNull Supplier<Future<?>> supplier) {
            Check.notNull(supplier, "supplier");
            this.supplier = supplier;
        }

        @Override
        protected Future<?> runFuture() {
            return this.supplier.get();
        }
    }

    public static class RunnableTaskImpl extends AbstractTask {

        private final Runnable runnable;
        private boolean done = false;

        public RunnableTaskImpl(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void run() {
            this.runnable.run();
            this.done = true;
        }

        @Override
        public void cancel() {

        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return this.done;
        }
    }
}
