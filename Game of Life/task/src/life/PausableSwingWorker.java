package life;

import javax.swing.*;

abstract class PausableSwingWorker<K, V> extends SwingWorker<K, V> {

    static final int MAX_SPEED = 510;
    static final int MIN_SPEED = 10;
    static final int INIT_SPEED = 250;
    static final int DEFAULT_SIZE = 20;

    private volatile boolean isPaused;
    private volatile boolean resetRequested;
    private volatile boolean resizeRequested = false;
    private int requestedSize = DEFAULT_SIZE;
    int speed = INIT_SPEED;

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public final boolean isResetRequested() {
        return resetRequested;
    }

    public final boolean isResizeRequested() {
        return resizeRequested;
    }

    public final void requestResize(int size) {
        if (!isResizeRequested() && !isDone()) {
            this.resizeRequested = true;
            // 20 minimum size 500 maximum size
            this.requestedSize = Math.min(Math.max(size, 20), 500);
        }
    }

    public int getRequestedSize() {
        return requestedSize;
    }

    public final void removeResizeRequest() {
        resizeRequested = false;
    }

    public final void removeResetRequest() {
        resetRequested = false;
    }

    public final void requestReset() {
        if (!isResetRequested() && !isDone()) {
            resetRequested = true;
        }
    }

    public final void pause() {
        if (!isPaused() && !isDone()) {
            isPaused = true;
        }
    }

    public final void resume() {
        if (isPaused() && !isDone()) {
            isPaused = false;
        }
    }

    public final boolean isPaused() {
        return isPaused;
    }
}
