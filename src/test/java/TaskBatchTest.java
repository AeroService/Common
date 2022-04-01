import de.natrox.common.concurrent.SimpleTaskBatchFactory;

public class TaskBatchTest {

    public void test() {
        var factory = new SimpleTaskBatchFactory();

        factory.createTaskBatch()
            .sync(() -> {

            })
            .async(() -> {

            })
            .execute();

    }

}
