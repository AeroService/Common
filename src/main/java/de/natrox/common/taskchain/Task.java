package de.natrox.common.taskchain;

public interface Task extends Runnable {

    @Override
    void run();
}
