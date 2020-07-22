package dev.deamsy.eventbus.api.event;

/**
 * Base interface for all cancellable events.
 *
 * @author deamsy
 * @since 1.0
 */
public interface ICancellableEvent {
    /**
     * @return whether or not this event has been cancelled
     */
    boolean isCancelled();

    /**
     * Sets whether or not this event has been cancelled.
     *
     * @param cancelled whether or not this event has been cancelled
     */
    void setCancelled(boolean cancelled);

    /**
     * Cancels this event.
     *
     * Same as {@code setCancelled(true)}.
     */
    default void cancel() {
        setCancelled(true);
    }
}
