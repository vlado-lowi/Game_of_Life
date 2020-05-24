package life;

import javax.swing.*;

abstract class PausableSwingWorker<K, V> extends SwingWorker<K, V> {

    private volatile boolean isPaused;
    private volatile boolean resetRequested;

    public final boolean isResetRequested() {
        return resetRequested;
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
