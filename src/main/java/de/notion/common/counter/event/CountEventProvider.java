package de.notion.common.counter.event;

import de.notion.common.counter.Counter;

public interface CountEventProvider {

    void onStart(Counter counter);

    void onTick(Counter counter);

    void onFinish(Counter counter);

    void onCancel(Counter counter);

}
