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

package de.natrox.serialize.objectmapping;

import de.natrox.common.function.ThrowableFunction;
import de.natrox.common.validate.Check;
import de.natrox.serialize.exception.SerializeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Supplier;

public interface FieldDiscoverer<T> {

    static @NotNull FieldDiscoverer<?> create() {
        return ObjectFieldDiscoverer.EMPTY_CONSTRUCTOR_INSTANCE;
    }

    static @NotNull FieldDiscoverer<?> create(@NotNull ThrowableFunction<Type, Supplier<Object>, SerializeException> instanceFactory) {
        Check.notNull(instanceFactory, "instanceFactory");
        return new ObjectFieldDiscoverer(instanceFactory, true);
    }

    <U> @Nullable InstanceFactory<T> discover(Type target, List<FieldInfo<U, T>> fields) throws SerializeException;

    interface InstanceFactory<T> {

        T begin();

        void complete(Object value, T intermediate) throws SerializeException;

        Object complete(T intermediate) throws SerializeException;

    }
}
