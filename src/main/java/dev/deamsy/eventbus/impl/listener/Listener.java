package dev.deamsy.eventbus.impl.listener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Listener<T> extends Comparable<Listener<?>> {
    @NotNull Class<@NotNull ? extends T> getEventClass();

    int getPriority();

    boolean matches(@NotNull Class<@NotNull ?> owner, @Nullable Object obj);

    void call(@NotNull T event) throws Throwable;

    @NotNull String describe();

    @Override
    default int compareTo(@NotNull Listener<@NotNull ?> o) {
        return Integer.compare(getPriority(), o.getPriority());
    }
}
