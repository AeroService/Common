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
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event bus.
 */
public sealed interface EventBus permits EventBusImpl {

    /**
     * Creates a new event bus.
     *
     * @return the created event bus
     */
    static @NotNull EventBus create() {
        return new EventBusImpl();
    }

    /**
     * Registers an {@link EventListener}.
     *
     * @param listener the event listener
     */
    void register(@NotNull EventListener<?> listener);

    /**
     * Registers an {@link EventListener} without any special options. The given listener will be executed if the event
     * passes all parent filtering.
     *
     * @param type    the event type to handle
     * @param handler the handler function
     * @param <T>     the event type to handle
     */
    default <T> void register(@NotNull Class<T> type, @NotNull Consumer<T> handler) {
        Check.notNull(type, "type");
        Check.notNull(handler, "handler");
        this.register(EventListener.of(type, handler));
    }

    /**
     * Unregisters an {@link EventListener}.
     *
     * @param listener the event listener
     */
    void unregister(@NotNull EventListener<?> listener);

    /**
     * Unregisters all {@link EventListener} that passed the condition.
     *
     * @param predicate the event listener
     */
    void unregisterIf(@NotNull Predicate<EventListener<?>> predicate);

    /**
     * Checks if the event bus has registered the event listener.
     *
     * @param listener the event listener
     * @return true, if the event bus has registered the event listener, false, if not
     */
    boolean has(@NotNull EventListener<?> listener);

    /**
     * Calls an event starting from this node.
     *
     * @param event the event to call
     */
    void call(@NotNull Object event);

    /**
     * Execute a cancellable event with a callback to execute if the event is successful. Event conditions and
     * propagation is the same as {@link #call(Object)}.
     *
     * @param event    the event to execute
     * @param callback a callback if the event is not cancelled
     */
    default void callCancellable(@NotNull Object event, @NotNull Runnable callback) {
        Check.notNull(event, "event");
        Check.notNull(callback, "callback");
        this.call(event);
        if (event instanceof CancellableEvent cancellableEvent && cancellableEvent.isCancelled()) {
            return;
        }
        callback.run();
    }
}
