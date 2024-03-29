/*
 * Copyright 2020-2023 AeroService
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

package org.aero.common.core.util;

/**
 * Miscellaneous {@link String} utility methods.
 */
public final class StringUtil {

    /**
     * Contains a space character as string.
     */
    public static final String SPACE = " ";

    /**
     * Contains a space character as char.
     */
    public static final char SPACE_CHAR = ' ';

    private StringUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns whether a string is null or empty or not.
     *
     * @param string the string to check
     * @return true, if the string is null or empty, false, if not.
     */
    public static boolean isNullOrEmpty(final String string) {
        return string == null || string.isEmpty();
    }
}
