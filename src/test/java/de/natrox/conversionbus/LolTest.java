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

package de.natrox.conversionbus;

import de.natrox.conversionbus.exception.SerializeException;
import de.natrox.conversionbus.convert.Converter;
import de.natrox.conversionbus.convert.Converters;
import io.leangen.geantyref.TypeToken;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class LolTest {

    @Test
    void test() throws SerializeException {
        boolean b1 = Converters.BOOLEAN.read("true");
        Object o1 = Converters.BOOLEAN.write(b1);

        Converter<Object, Boolean> converter = ConversionBus.defaults().get(boolean.class);
        boolean b2 = converter.read("true");
        Object o2 = converter.write(b2);

        Converter<Map<String, Object>, Object> converter1 = ConversionBus.defaults().get(new TypeToken<Map<String, Object>>() {}.getType(), Object.class);
    }
}
