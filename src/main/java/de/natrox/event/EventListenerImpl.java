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

package de.natrox.event;

import de.natrox.common.validate.Check;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ClassCanBeRecord")
final class EventListenerImpl<T> implements EventListener<T> {

    private final Class<T> type;
    private final List<Predicate<T>> conditions;
    private final Consumer<T> handler;

    public EventListenerImpl(Class<T> type, List<Predicate<T>> conditions, Consumer<T> handler) {
        this.type = type;
        this.conditions = conditions;
        this.handler = handler;
    }

    @Override
    public @NotNull Class<T> eventType() {
        return this.type;
    }

    @Override
    public void handle(@NotNull T event) {
        if (event instanceof CancellableEvent cancellableEvent && cancellableEvent.isCancelled()) {
            return;
        }

        if (!this.conditions.isEmpty()) {
            for (var condition : this.conditions) {
                if (!condition.test(event)) {
                    return;
                }
            }
        }

        if (this.handler == null) {
            return;
        }
        this.handler.accept(event);
    }

    static final class BuilderImpl<T> implements EventListener.Builder<T> {

        private final Class<T> type;
        private final List<Predicate<T>> conditions;
        private Consumer<T> handler;

        public BuilderImpl(Class<T> type) {
            this.type = type;
            this.conditions = new ArrayList<>();
        }

        @Override
        public EventListener.@NotNull Builder<T> condition(@NotNull Predicate<T> condition) {
            Check.notNull(condition, "condition");
            this.conditions.add(condition);
            return this;
        }

        @Override
        public EventListener.@NotNull Builder<T> handler(@NotNull Consumer<T> handler) {
            Check.notNull(handler, "handler");
            this.handler = handler;
            return this;
        }

        @Override
        public EventListener<T> build() {
            return new EventListenerImpl<>(this.type, new ArrayList<>(this.conditions), this.handler);
        }
    }
}
