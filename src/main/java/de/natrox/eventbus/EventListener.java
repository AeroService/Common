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

import de.natrox.common.builder.IBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface EventListener<T> {

    static <T> @NotNull Builder<T> builder(@NotNull Class<T> type) {
        return new EventListenerBuilderImpl<>(type);
    }

    static <T> @NotNull EventListener<T> of(@NotNull Class<T> type, @NotNull Consumer<T> handler) {
        return EventListener.builder(type).handler(handler).build();
    }

    @NotNull Class<T> eventType();

    void handle(T event);

    sealed interface Builder<T> extends IBuilder<EventListener<T>> permits EventListenerBuilderImpl {

        @NotNull Builder<T> condition(@NotNull Predicate<T> condition);

        @NotNull Builder<T> handler(@NotNull Consumer<T> handler);

    }

}
