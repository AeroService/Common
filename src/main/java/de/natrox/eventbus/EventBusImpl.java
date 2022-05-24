/*
 * Copyright 2020-2022 NatroxMC team
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

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;

@SuppressWarnings({"rawtypes", "unchecked"})
final class EventBusImpl implements EventBus {

    private final Set<ListenerEntry> listeners;

    EventBusImpl() {
        this.listeners = new CopyOnWriteArraySet<>();
    }

    @Override
    public void register(@NotNull Class<?> type, @NotNull EventListener<?> eventListener) {
        this.listeners.add(new ListenerEntry(type, eventListener));
    }

    @Override
    public void unregister(@NotNull EventListener<?> eventListener) {
        this.listeners.removeIf(entry -> entry.listener().equals(eventListener));
    }

    @Override
    public void unregisterIf(@NotNull Predicate<EventListener<?>> predicate) {
        this.listeners.removeIf(entry -> predicate.test(entry.listener()));
    }

    @Override
    public boolean listening(@NotNull EventListener<?> listener) {
        for (var entry : this.listeners) {
            if (entry.listener().equals(listener))
                return true;
        }
        return false;
    }

    @Override
    public void call(@NotNull Object event) {
        for (var entry : this.listeners) {
            if (!entry.type().isAssignableFrom(event.getClass()))
                continue;
            entry.listener().handle(event);
        }
    }

    private record ListenerEntry(Class type, EventListener listener) {

    }
}
