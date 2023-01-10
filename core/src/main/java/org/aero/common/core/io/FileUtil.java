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

package org.aero.common.core.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Deprecated
public final class FileUtil {

    //TODO: Renovate

    @Deprecated
    public static boolean createDirectory(File path) {
        if (!path.exists()) {
            return path.mkdirs();
        }

        return false;
    }

    @Deprecated
    public static boolean createDirectory(Path path) {
        try {
            Files.createDirectories(path);
            return true;
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
    }

}
