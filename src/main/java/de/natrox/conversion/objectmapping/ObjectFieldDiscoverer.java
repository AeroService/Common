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

package de.natrox.conversion.objectmapping;

import de.natrox.common.function.ThrowableFunction;
import de.natrox.conversion.exception.SerializeException;
import io.leangen.geantyref.GenericTypeReflector;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static io.leangen.geantyref.GenericTypeReflector.*;

@SuppressWarnings("ClassCanBeRecord")
final class ObjectFieldDiscoverer implements FieldDiscoverer<Map<Field, Object>> {

    static final ObjectFieldDiscoverer EMPTY_CONSTRUCTOR_INSTANCE = new ObjectFieldDiscoverer(new EmptyConstructorFactory(), false);

    private final ThrowableFunction<Type, Supplier<Object>, SerializeException> instanceFactory;
    private final boolean requiresInstanceCreation;

    ObjectFieldDiscoverer(ThrowableFunction<Type, Supplier<Object>, SerializeException> instanceFactory, boolean requiresInstanceCreation) {
        this.instanceFactory = instanceFactory;
        this.requiresInstanceCreation = requiresInstanceCreation;
    }

    @Override
    public <U> InstanceFactory<Map<Field, Object>> discover(Type target, List<FieldInfo<U, Map<Field, Object>>> fields) throws SerializeException {
        Class<?> clazz = GenericTypeReflector.erase(target);
        if (clazz.isInterface()) {
            throw new SerializeException(target, "ObjectMapper can only work with concrete types");
        }

        Supplier<Object> maker = this.instanceFactory.apply(target);
        if (maker == null && this.requiresInstanceCreation) {
            return null;
        }

        Type collectType = target;
        Class<?> collectClass = clazz;
        while (true) {
            collectFields(collectType, fields);
            collectClass = collectClass.getSuperclass();
            if (collectClass.equals(Object.class)) {
                break;
            }
            collectType = getExactSuperType(collectType, collectClass);
        }

        return new InstanceFactory<>() {
            @Override
            public Map<Field, Object> begin() {
                return new HashMap<>();
            }

            @Override
            public void complete(Object value, Map<Field, Object> intermediate) throws SerializeException {
                for (Map.Entry<Field, Object> entry : intermediate.entrySet()) {
                    try {
                        entry.getKey().set(value, entry.getValue());
                    } catch (IllegalAccessException e) {
                        throw new SerializeException(target, e);
                    }
                }
            }

            @Override
            public Object complete(Map<Field, Object> intermediate) throws SerializeException {
                Object instance = maker == null ? null : maker.get();
                if (instance == null) {
                    throw new SerializeException(target, "Unable to create instances for this type");
                }
                this.complete(instance, intermediate);
                return instance;
            }
        };
    }

    private <U> void collectFields(Type clazz, List<FieldInfo<U, Map<Field, Object>>> fields) {
        for (Field field : erase(clazz).getDeclaredFields()) {
            if ((field.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT)) != 0) {
                continue;
            }

            field.setAccessible(true);
            Type fieldType = getFieldType(field, clazz);
            fields.add(new FieldInfo<>(
                field.getName(),
                fieldType,
                (intermediate, value) -> intermediate.put(field, value),
                field::get
            ));
        }
    }

    final static class EmptyConstructorFactory implements ThrowableFunction<Type, Supplier<Object>, SerializeException> {

        @Override
        public Supplier<Object> apply(Type type) {
            try {
                Constructor<?> constructor;
                constructor = erase(type).getDeclaredConstructor();
                constructor.setAccessible(true);
                return () -> {
                    try {
                        return constructor.newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                };
            } catch (NoSuchMethodException e) {
                return null;
            }
        }
    }
}
