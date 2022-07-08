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

package de.natrox.serialize.util;

public final class NumberUtil {

    private NumberUtil() {
        throw new UnsupportedOperationException();
    }

    public static <T extends Number> T cast(Number number, Class<T> to) {
        Class<?> resultClass = boxType(to);
        if (Byte.class.equals(resultClass)) return to.cast(number.byteValue());
        if (Short.class.equals(resultClass)) return to.cast(number.shortValue());
        if (Integer.class.equals(resultClass)) return to.cast(number.intValue());
        if (Float.class.equals(resultClass)) return to.cast(number.floatValue());
        if (Long.class.equals(resultClass)) return to.cast(number.longValue());
        if (Double.class.equals(resultClass)) return to.cast(number.doubleValue());
        throw new ClassCastException("Unable to cast " + Number.class.getName() + " to " + to.getName() + ".");
    }

    public static Class<?> boxType(Class<?> primitive) {
        if (void.class.equals(primitive)) return Void.class;
        if (byte.class.equals(primitive)) return Byte.class;
        if (short.class.equals(primitive)) return Short.class;
        if (int.class.equals(primitive)) return Integer.class;
        if (float.class.equals(primitive)) return Float.class;
        if (long.class.equals(primitive)) return Long.class;
        if (double.class.equals(primitive)) return Double.class;
        if (char.class.equals(primitive)) return Character.class;
        return primitive;
    }

    public static Class<?> primitiveType(Class<?> box) {
        if (Void.class.equals(box)) return void.class;
        if (Byte.class.equals(box)) return byte.class;
        if (Short.class.equals(box)) return short.class;
        if (Integer.class.equals(box)) return int.class;
        if (Float.class.equals(box)) return float.class;
        if (Long.class.equals(box)) return long.class;
        if (Double.class.equals(box)) return double.class;
        if (Character.class.equals(box)) return char.class;
        return box;
    }

    public static boolean castable(Class<?> to) {
        return Number.class.isAssignableFrom(to);
    }
}
