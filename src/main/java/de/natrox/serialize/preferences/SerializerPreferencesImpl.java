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

package de.natrox.serialize.preferences;

import java.util.Collections;
import java.util.List;

public class SerializerPreferencesImpl implements SerializerPreferences {

    private boolean lenient;
    private List<Class<?>> acceptedTypes;

    SerializerPreferencesImpl() {
        this.lenient = false;
        this.acceptedTypes = List.of(Boolean.class, Number.class, CharSequence.class);
    }

    @Override
    public boolean lenient() {
        return this.lenient;
    }

    @Override
    public SerializerPreferencesImpl lenient(boolean lenient) {
        this.lenient = lenient;
        return this;
    }

    @Override
    public List<Class<?>> acceptedTypes() {
        return Collections.unmodifiableList(this.acceptedTypes);
    }

    @Override
    public SerializerPreferencesImpl acceptedTypes(List<Class<?>> acceptedTypes) {
        this.acceptedTypes = acceptedTypes;
        return this;
    }
}
