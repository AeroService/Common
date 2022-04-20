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

import de.natrox.common.scheduler.Scheduler;
import de.natrox.common.scheduler.Task;
import de.natrox.common.scheduler.TaskStatus;

import java.util.concurrent.TimeUnit;

public class SchedulerTest {

    public static void main(String[] args) {
        Scheduler scheduler = Scheduler.create();

        scheduler
            .buildTask(() -> {
                System.out.println("Test");
            }).schedule();

        scheduler
            .buildTask(() -> {
                System.out.println("Test");
            })
            .delay(5, TimeUnit.SECONDS)
            .schedule();

        Task task = scheduler
            .buildTask(() -> {
                System.out.println("Test");
            })
            .repeat(5, TimeUnit.SECONDS)
            .delay(10, TimeUnit.SECONDS)
            .schedule();

        Scheduler owner = task.owner();
        TaskStatus status = task.status();
        switch (status) {
            case SCHEDULED -> {
                System.out.println("Task is scheduled");
            }
            case CANCELLED -> {
                System.out.println("Task is canceled");
            }
            case FINISHED -> {
                System.out.println("Task is finished");
            }
        }

        task.cancel();
    }

}
