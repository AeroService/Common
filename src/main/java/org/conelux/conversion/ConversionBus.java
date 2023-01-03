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

package org.conelux.conversion;

import org.conelux.common.builder.IBuilder;
import io.leangen.geantyref.TypeToken;
import org.conelux.conversion.exception.ConversionException;
import org.jetbrains.annotations.NotNull;

public sealed interface ConversionBus permits ConversionBusImpl {

    static @NotNull ConversionBus.Builder builder() {
        return new ConversionBusImpl.BuilderImpl();
    }

    default ConversionBus.Builder childBuilder() {
        return new ConversionBusImpl.BuilderImpl();
    }

    <T> @NotNull T convert(@NotNull Object source, @NotNull Class<T> targetType) throws ConversionException;

    interface Builder extends ConverterRegistry<Builder>, IBuilder<ConversionBus> {

    }
}
