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

import java.util.function.Consumer;
import java.util.function.Predicate;

public sealed interface EventBus permits EventBusImpl {

    static @NotNull EventBus create() {
        return new EventBusImpl();
    }

    void register(@NotNull EventListener<?> listener);

    default <T> void register(@NotNull Class<T> type, @NotNull Consumer<T> consumer) {
        this.register(EventListener.of(type, consumer));
    }

    void unregister(@NotNull EventListener<?> listener);

    void unregisterIf(@NotNull Predicate<EventListener<?>> predicate);

    boolean listening(@NotNull EventListener<?> type);

    void call(@NotNull Object event);

}
