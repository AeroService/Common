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

import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.UUID;
import org.conelux.conversion.converter.EnumToIntegerConverter;
import org.conelux.conversion.converter.EnumToStringConverter;
import org.conelux.conversion.converter.ObjectToStringConverter;
import org.conelux.conversion.converter.StringToBooleanConverter;
import org.conelux.conversion.converter.StringToCharacterConverter;
import org.conelux.conversion.converter.StringToCharsetConverter;
import org.conelux.conversion.converter.StringToCurrencyConverter;
import org.conelux.conversion.converter.StringToEnumConverterFactory;
import org.conelux.conversion.converter.StringToNumberConverterFactory;
import org.conelux.conversion.converter.StringToUUIDConverter;
import org.conelux.conversion.exception.ConversionException;
import org.junit.jupiter.api.Test;

class ConversionBusTest {

    @Test
    void test() {
        ConversionBus conversionBus = ConversionBus
            .builder()
            // -> Number
            .register(String.class, Number.class, new StringToNumberConverterFactory())
            // -> Integer
            .register(Enum.class, Integer.class, new EnumToIntegerConverter())
            // -> Boolean
            .register(String.class, Boolean.class, new StringToBooleanConverter())
            // -> String
            .register(Number.class, String.class, new ObjectToStringConverter())
            .register(Character.class, String.class, new ObjectToStringConverter())
            .register(Boolean.class, String.class, new ObjectToStringConverter())
            .register(Enum.class, String.class, new EnumToStringConverter())
            .register(Locale.class, String.class, new ObjectToStringConverter())
            .register(Charset.class, String.class, new ObjectToStringConverter())
            .register(Currency.class, String.class, new ObjectToStringConverter())
            .register(UUID.class, String.class, new ObjectToStringConverter())
            // -> Character
            .register(String.class, Character.class, new StringToCharacterConverter())
            // -> Charset
            .register(String.class, Charset.class, new StringToCharsetConverter())
            // -> Currency
            .register(String.class, Currency.class, new StringToCurrencyConverter())
            // -> UUID
            .register(String.class, UUID.class, new StringToUUIDConverter())
            // -> Enum
            .register(String.class, Enum.class, new StringToEnumConverterFactory())
            .build();

        try {
            int count = conversionBus.convert("20", Integer.class);

            System.out.println(count);
        } catch (ConversionException e) {
            throw new RuntimeException(e);
        }
    }

    enum Mood {

        HAPPY,
        SAD

    }
}
