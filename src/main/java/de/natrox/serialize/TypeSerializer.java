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

package de.natrox.serialize;

import de.natrox.common.validate.Check;
import de.natrox.serialize.exception.SerializeException;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;

public abstract class TypeSerializer<T> implements Serializer<T> {

    private final TypeToken<T> typeToken;

    protected TypeSerializer(TypeToken<T> typeToken) {
        this.typeToken = typeToken;
    }

    protected TypeSerializer(Class<T> type) {
        this.typeToken = TypeToken.get(type);
    }

    public TypeToken<T> type() {
        return this.typeToken;
    }

    public @NotNull T deserialize(@NotNull Object obj) throws SerializeException {
        Check.notNull(obj, "object");
        return this.deserialize(obj, this.typeToken);
    }
}
