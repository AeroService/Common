import de.natrox.common.scheduler.Scheduler;
import de.natrox.common.scheduler.TaskSchedule;

import java.util.concurrent.TimeUnit;

public class SchedulerTest {

    public void test() {
        Scheduler scheduler = Scheduler.create();

        scheduler.buildTask(() -> {

        }).delay(TaskSchedule.seconds(50)).schedule();

        scheduler.buildTask(() -> {

        }).repeat(TaskSchedule.seconds(5)).schedule();
    }

}
