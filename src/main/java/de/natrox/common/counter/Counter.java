package de.natrox.common.counter;

public sealed interface Counter permits Countdown, Timer  {

    void start();

    void pause();

    void resume();

    void stop();

    int currentTime();

    boolean isPaused();

    void setPaused(boolean paused);

    boolean isRunning();

    void setRunning(boolean running);

}
