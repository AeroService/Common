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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
        return type;
    }

    @Override
    public void handle(@NotNull T event) {
        if(!this.conditions.isEmpty()) {
            for(var condition : conditions) {
                if(!condition.test(event)) {
                    return;
                }
            }
        }

        if(this.handler != null)
            this.handler.accept(event);
    }
}
