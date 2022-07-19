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

package de.natrox.serialize.parse;

import de.natrox.serialize.exception.SerializeException;

import java.util.Locale;
import java.util.function.BiFunction;

abstract class NumericParser<T> implements Parser<T> {

    protected final static float EPSILON = Float.MIN_NORMAL;

    static <T extends Number> T parseNumber(String input,
                                            final BiFunction<String, Integer, T> parseFunc, final BiFunction<String, Integer, T> unsignedParseFunc,
                                            final String suffix) throws SerializeException {
        boolean unsigned = false;
        boolean negative = false;

        int startIdx = 0;
        int endIdx = input.length();

        // type suffix
        if (input.endsWith(suffix) || input.endsWith(suffix.toUpperCase(Locale.ROOT))) {
            --endIdx;
        }

        // unsigned
        if (endIdx > 0 && input.charAt(endIdx - 1) == 'u') {
            unsigned = true;
            --endIdx;
        }

        if (endIdx > startIdx && input.charAt(startIdx) == '-') {
            if (unsigned) {
                throw new SerializeException("Negative numbers cannot be unsigned! (both - prefix and u suffix were used)");
            }
            negative = true;
            ++startIdx;
        } else if (endIdx > startIdx && input.charAt(startIdx) == '+') { // skip, positive is the default
            ++startIdx;
        }

        // bases
        int radix = 10;
        if (input.startsWith("0x", startIdx)) { // hex
            radix = 16;
            startIdx += 2;
        } else if (input.length() > startIdx && input.charAt(startIdx) == '#') { // hex
            radix = 16;
            ++startIdx;
        } else if (input.startsWith("0b", startIdx)) { // binary
            radix = 2;
            startIdx += 2;
        }

        input = input.substring(startIdx, endIdx);

        if (negative) { // ugly but not super avoidable without knowing the number type
            input = "-" + input;
        }
        try {
            return (unsigned ? unsignedParseFunc : parseFunc).apply(input, radix);
        } catch (final IllegalArgumentException ex) {
            throw new SerializeException(ex);
        }
    }
}
