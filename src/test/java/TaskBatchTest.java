/*
 * Copyright 2020-2022 NatroxMC team
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

import de.natrox.common.batch.SimpleTaskBatchFactory;

import java.util.concurrent.TimeUnit;

public class TaskBatchTest {

    public static void main(String[] args) {
        new TaskBatchTest().test();
    }

    public void test() {
        var factory = new SimpleTaskBatchFactory();

        factory.createTaskBatch()
            .sync(() -> {
                System.out.println("1");
            })
            .async(() -> {
                System.out.println("2");
            })
            .wait(10, TimeUnit.SECONDS)
            .sync(() -> {
                System.out.println("3");
            })
            .execute();

    }

}
