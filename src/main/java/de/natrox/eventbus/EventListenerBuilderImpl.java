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

package de.natrox.eventbus;

import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

final class EventListenerBuilderImpl<T> implements EventListener.Builder<T> {

    private final Class<T> type;
    private final List<Predicate<T>> conditions;
    private Consumer<T> handler;

    public EventListenerBuilderImpl(Class<T> type) {
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
        final List<Predicate<T>> predicates = new ArrayList<>(this.conditions);
        final Consumer<T> handler = this.handler;

        return new EventListener<>() {
            @Override
            public @NotNull Class<T> eventType() {
                return EventListenerBuilderImpl.this.type;
            }

            @Override
            public void handle(@NotNull T event) {
                if(!predicates.isEmpty()) {
                    for(var predicate : predicates) {
                        if(!predicate.test(event)) {
                            return;
                        }
                    }
                }

                if(handler != null)
                    handler.accept(event);
            }
        };
    }
}
