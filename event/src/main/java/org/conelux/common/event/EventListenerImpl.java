/*
 * Copyright 2020-2022 Conelux
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

package org.conelux.common.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.conelux.common.core.validate.Check;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ClassCanBeRecord")
final class EventListenerImpl<T> implements EventListener<T> {

    static int DEFAULT_PRIORITY = 0;

    private final Class<T> type;
    private final List<Predicate<T>> conditions;
    private final int priority;
    private final Consumer<T> handler;

    public EventListenerImpl(Class<T> type, List<Predicate<T>> conditions, int priority, Consumer<T> handler) {
        this.type = type;
        this.conditions = conditions;
        this.priority = priority;
        this.handler = handler;
    }

    @Override
    public @NotNull Class<T> eventType() {
        return this.type;
    }

    @Override
    public int priority() {
        return this.priority;
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

    @Override
    public int compareTo(@NotNull EventListener<T> other) {
        return Integer.compare(this.priority, other.priority());
    }

    static final class BuilderImpl<T> implements EventListener.Builder<T> {

        private final Class<T> type;
        private final List<Predicate<T>> conditions;
        private int priority = EventListenerImpl.DEFAULT_PRIORITY;
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
        public @NotNull Builder<T> priority(int priority) {
            this.priority = priority;
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
            return new EventListenerImpl<>(this.type, new ArrayList<>(this.conditions), this.priority, this.handler);
        }
    }
}
