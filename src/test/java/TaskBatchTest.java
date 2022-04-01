import de.natrox.common.concurrent.SimpleTaskBatchFactory;

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
