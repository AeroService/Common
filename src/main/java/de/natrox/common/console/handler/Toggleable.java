package de.natrox.common.console.handler;

public abstract class Toggleable {

    private boolean enabled = true;

    public boolean enabled() {
        return this.enabled;
    }

    public void enabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }

}
