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

package de.natrox.conversionbus.exception;

import java.lang.reflect.Type;

public class ConversionFailedException extends SerializeException {

    public ConversionFailedException(Object inputValue, String typeDescription) {
        super("Failed to convert input value of type " + inputValue.getClass() + " to " + typeDescription);
    }

    public ConversionFailedException(Type target, Object inputValue, String typeDescription) {
        super(target, "Failed to convert input value of type " + inputValue.getClass() + " to " + typeDescription);
    }
}
