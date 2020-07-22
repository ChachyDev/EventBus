package dev.deamsy.eventbus.api.event;

/**
 * Base class for cancellable events.
 *
 * @author deamsy
 * @since 1.0
 */
public abstract class CancellableEvent implements ICancellableEvent {
    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
