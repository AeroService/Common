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
