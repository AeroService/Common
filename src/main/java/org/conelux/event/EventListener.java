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

import org.conelux.common.builder.IBuilder;
import org.conelux.common.validate.Check;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event listener (handler) in an event graph.
 * <p>
 * A listener is responsible for executing some action based on an event triggering.
 *
 * @param <T> the event type being handled
 */
public interface EventListener<T> extends Comparable<EventListener<T>> {

    /**
     * Created a new {@link Builder} for an event listener.
     *
     * @param type the type of event the event listener is listening to
     * @param <T>  the event type being handled
     * @return the created event listener builder
     */
    static <T> @NotNull Builder<T> builder(@NotNull Class<T> type) {
        return new EventListenerImpl.BuilderImpl<>(type);
    }

    /**
     * Create an event listener without any special options. The given listener will be executed if the event passes all
     * parent filtering.
     *
     * @param type    the event type to handle
     * @param handler the handler function
     * @param <T>     the event type to handle
     * @return an event listener with the given properties
     */
    static <T> @NotNull EventListener<T> of(@NotNull Class<T> type, @NotNull Consumer<T> handler) {
        Check.notNull(type, "type");
        Check.notNull(handler, "handler");
        return EventListener.builder(type).handler(handler).build();
    }

    /**
     * Returns the type of the event listener is listening to
     *
     * @return the type of the event
     */
    @NotNull Class<T> eventType();

    /**
     * Returns the priority of the event listener
     *
     * @return the priority
     */
    int priority();

    @ApiStatus.Internal
    void handle(@NotNull T event);

    /**
     * Represent a build for an event listener.
     *
     * @param <T> the type of event that the event listener will handle
     */
    sealed interface Builder<T> extends IBuilder<EventListener<T>> permits EventListenerImpl.BuilderImpl {

        /**
         * Adds a filter to the executor of this listener. The executor will only be called if this condition passes on
         * the given event.
         *
         * @param condition the condition used to filter
         * @return this builder, for chaining
         */
        @NotNull Builder<T> condition(@NotNull Predicate<T> condition);

        /**
         * Add a priority to this listener. Executors with higher priorities will receive events before others with a
         * lower priority. The default priority is 0.
         *
         * @param priority the priority
         * @return this builder, for chaining
         */
        @NotNull Builder<T> priority(int priority);

        /**
         * Sets the handler for this event listener. This will be executed if the listener passes all conditions.
         *
         * @param handler the handler that gets executed
         * @return this builder, for chaining
         */
        @NotNull Builder<T> handler(@NotNull Consumer<T> handler);

    }
}
