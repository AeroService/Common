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

import de.natrox.common.container.Pair;
import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;

@SuppressWarnings({"rawtypes"})
final class EventBusImpl implements EventBus {

    private final Set<Pair<Class, EventListener>> listeners;

    EventBusImpl() {
        this.listeners = new CopyOnWriteArraySet<>();
    }

    @Override
    public void register(@NotNull EventListener<?> listener) {
        Check.notNull(listener, "listener");
        this.listeners.add(Pair.of(listener.eventType(), listener));
    }

    @Override
    public void unregister(@NotNull EventListener<?> listener) {
        Check.notNull(listener, "listener");
        this.listeners.removeIf(entry -> entry.second().equals(listener));
    }

    @Override
    public void unregisterIf(@NotNull Predicate<EventListener<?>> predicate) {
        Check.notNull(predicate, "predicate");
        this.listeners.removeIf(entry -> predicate.test(entry.second()));
    }

    @Override
    public boolean has(@NotNull EventListener<?> listener) {
        Check.notNull(listener, "listener");
        for (var entry : this.listeners) {
            if (entry.second().equals(listener))
                return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void call(@NotNull Object event) {
        Check.notNull(event, "event");
        for (var entry : this.listeners) {
            if (!entry.first().isAssignableFrom(event.getClass()))
                continue;
            entry.second().handle(event);
        }
    }
}
