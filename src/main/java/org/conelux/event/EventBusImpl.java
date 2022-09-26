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

package org.conelux.event;

import org.conelux.common.validate.Check;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"rawtypes"})
final class EventBusImpl implements EventBus {

    private final Map<Class, SortedSet<EventListener>> listeners;

    EventBusImpl() {
        this.listeners = new ConcurrentHashMap<>();
    }

    @Override
    public void register(@NotNull EventListener<?> listener) {
        Check.notNull(listener, "listener");
        SortedSet<EventListener> typeListeners = this.listeners.computeIfAbsent(
            listener.eventType(),
            type -> new TreeSet<>()
        );
        typeListeners.add(listener);
    }

    @Override
    public void unregister(@NotNull EventListener<?> listener) {
        Check.notNull(listener, "listener");
        SortedSet<EventListener> typeListeners = this.listeners.get(listener.eventType());

        if (typeListeners == null) {
            return;
        }

        typeListeners.remove(listener);
    }

    @Override
    public void unregisterIf(@NotNull Predicate<EventListener<?>> predicate) {
        Check.notNull(predicate, "predicate");
        for (SortedSet<EventListener> typeListeners : this.listeners.values()) {
            typeListeners.removeIf(predicate::test);
        }
    }

    @Override
    public boolean has(@NotNull EventListener<?> listener) {
        Check.notNull(listener, "listener");
        SortedSet<EventListener> typeListeners = this.listeners.get(listener.eventType());

        if (typeListeners == null) {
            return false;
        }

        return typeListeners.contains(listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void call(@NotNull Object event) {
        Check.notNull(event, "event");

        for (var entry : this.listeners.entrySet()) {
            if (!entry.getKey().isAssignableFrom(event.getClass())) {
                continue;
            }

            SortedSet<EventListener> typeListeners = entry.getValue();

            for (EventListener listener : typeListeners) {
                listener.handle(event);
            }
        }
    }
}
