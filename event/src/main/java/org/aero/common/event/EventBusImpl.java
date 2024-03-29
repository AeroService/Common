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

package org.aero.common.event;

import org.aero.common.core.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@SuppressWarnings({"rawtypes"})
final class EventBusImpl implements EventBus {

    private final Map<Class, SortedSet<EventListener>> listeners;

    EventBusImpl() {
        this.listeners = new ConcurrentHashMap<>();
    }

    @Override
    public void register(@NotNull final EventListener<?> listener) {
        Check.notNull(listener, "listener");
        final SortedSet<EventListener> typeListeners = this.listeners.computeIfAbsent(
            listener.eventType(),
            type -> new TreeSet<>()
        );
        typeListeners.add(listener);
    }

    @Override
    public void unregister(@NotNull final EventListener<?> listener) {
        Check.notNull(listener, "listener");
        final SortedSet<EventListener> typeListeners = this.listeners.get(listener.eventType());

        if (typeListeners == null) {
            return;
        }

        typeListeners.remove(listener);
    }

    @Override
    public void unregisterIf(@NotNull final Predicate<EventListener<?>> predicate) {
        Check.notNull(predicate, "predicate");
        for (final SortedSet<EventListener> typeListeners : this.listeners.values()) {
            typeListeners.removeIf(predicate::test);
        }
    }

    @Override
    public boolean has(@NotNull final EventListener<?> listener) {
        Check.notNull(listener, "listener");
        final SortedSet<EventListener> typeListeners = this.listeners.get(listener.eventType());

        if (typeListeners == null) {
            return false;
        }

        return typeListeners.contains(listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void call(@NotNull final Object event) {
        Check.notNull(event, "event");

        for (final Map.Entry<Class, SortedSet<EventListener>> entry : this.listeners.entrySet()) {
            if (!entry.getKey().isAssignableFrom(event.getClass())) {
                continue;
            }

            final SortedSet<EventListener> typeListeners = entry.getValue();

            for (final EventListener listener : typeListeners) {
                if (!(listener instanceof EventListenerImpl handler)) {
                    continue;
                }

                handler.handle(event);
            }
        }
    }
}
