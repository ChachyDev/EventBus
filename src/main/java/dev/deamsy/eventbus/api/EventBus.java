package dev.deamsy.eventbus.api;

import dev.deamsy.eventbus.api.listener.LambdaEventListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Event bus interface providing basic methods required by all event bus implementations.
 *
 * @author deamsy
 * @since 1.0
 */
public interface EventBus {
    /**
     * Registers a lambda listener to this event bus.
     *
     * @param eventClass the event class to listen for
     * @param listener the listener implementation
     * @return an event listener ID which can be used to unregister this listener later
     */
    <T> int registerLambda(@NotNull Class<@NotNull T> eventClass, @NotNull LambdaEventListener<@NotNull T> listener);

    /**
     * Registers a lambda listener to this event bus.
     *
     * @param eventClass the event class to listen for
     * @param listener the listener implementation
     * @param priority the listener priority
     * @return an event listener ID which can be used to unregister this listener later
     */
    <T> int registerLambda(@NotNull Class<@NotNull T> eventClass, @NotNull LambdaEventListener<@NotNull T> listener, int priority);

    /**
     * Registers an object's methods to this event bus.
     * Unlike {@link #register(Class)}, this method will register virtual methods, resulting in them being called
     * on the object passed to this method.
     *
     * Same thing as {@code register(object.getClass(), object)}.
     *
     * @param object the object to register
     * @return a list of event listener IDs which can be used to unregister them later
     */
    @NotNull int[] register(@NotNull Object object);

    /**
     * Registers a class's static methods to this event bus.
     * Unlike {@link #register(Object)}, this method will not register virtual methods.
     *
     * Same thing as {@code register(clazz, null)}.
     *
     * @param clazz the class to register
     * @return a list of event listener IDs which can be used to unregister them later
     */
    @NotNull int[] register(@NotNull Class<?> clazz);

    /**
     * Registers a class's methods to this event bus.
     * If {@code object} isn't {@code null}, this method will also register all virtual listener methods, resulting in
     * them being called on the object passed to this method.
     *
     * @param clazz the class to register
     * @param object the object to register
     * @return a list of event listener IDs which can be used to unregister them later
     */
    <@NotNull T> int[] register(@NotNull Class<T> clazz, @Nullable T object);

    /**
     * Unregisters an object's virtual methods from this event bus.
     *
     * Same as {@code unregister(object.getClass(), object)}.
     *
     * @param object the object to unregister
     */
    void unregister(@NotNull Object object);

    /**
     * Unregisters a class's static methods from this event bus.
     *
     * Same as {@code unregister(clazz, null)}.
     *
     * @param clazz the class to unregister
     */
    void unregister(@NotNull Class<?> clazz);

    /**
     * Unregisters a class's methods from this event bus.
     *
     * If {@code object} isn't {@code null}, this method will only unregister virtual listener methods that were registered
     * with {@code object}.
     *
     * @param clazz the class to unregister
     * @param object the object to unregister
     */
    <@NotNull T> void unregister(@NotNull Class<T> clazz, @Nullable T object);

    /**
     * Unregisters a listener by ID.
     *
     * @param id the listener ID to unregister
     */
    void unregister(int id);

    /**
     * Unregisters an array of listener IDs.
     *
     * @param ids the listener IDs to unregister
     */
    default void unregister(@NotNull int[] ids) {
        for (int id : ids) {
            unregister(id);
        }
    }

    /**
     * Posts an event to this event bus.
     *
     * @param event the event to post
     * @throws IllegalStateException if one of the listeners failed to process the event
     * @return the event posted
     */
    <T> @NotNull T post(@NotNull T event);
}
