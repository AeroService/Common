import de.natrox.common.scheduler.Scheduler;

import java.util.concurrent.TimeUnit;

public class SchedulerTest {

    public static void main(String[] args) {
        Scheduler scheduler = Scheduler.create();

        /*
        scheduler
            .buildTask(() -> {
                System.out.println("Test");
            })
            .delay(5, TimeUnit.SECONDS)
            .schedule();

         */

        scheduler
            .buildTask(() -> {
                System.out.println("Test");
            })
            .repeat(5, TimeUnit.SECONDS)
            .delay(10, TimeUnit.SECONDS)
            .schedule();
    }

}
