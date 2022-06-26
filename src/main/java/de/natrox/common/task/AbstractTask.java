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

package de.natrox.common.task;

import java.util.concurrent.Future;

abstract non-sealed class AbstractTask implements Task {

    public abstract void run();

    public abstract void cancel();

    abstract static class FutureTask extends AbstractTask {

        private Future<?> future;

        abstract Future<?> runFuture();

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
            if (this.future == null)
                return;

            this.future.cancel(false);
        }
    }
}
