package de.natrox.common.counter.event;

import de.natrox.common.counter.Counter;

public interface EventHandler {

    void onStart(Counter counter);

    void onTick(Counter counter);

    void onFinish(Counter counter);

    void onCancel(Counter counter);

}
