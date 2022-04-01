import de.natrox.common.concurrent.SimpleTaskBatchFactory;

import java.util.concurrent.TimeUnit;

public class TaskBatchTest {

    public void test() {
        var factory = new SimpleTaskBatchFactory();

        factory.createTaskBatch()
            .sync(() -> {

            })
            .async(() -> {

            })
            .wait(10, TimeUnit.SECONDS)
            .sync(() -> {

            })
            .execute();

    }

}
