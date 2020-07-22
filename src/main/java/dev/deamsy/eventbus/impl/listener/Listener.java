package dev.deamsy.eventbus.impl.listener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Listener<T> extends Comparable<Listener<?>> {
    @NotNull Class<? extends T> getEventClass();

    int getPriority();

    boolean matches(@NotNull Class<?> owner, @Nullable Object obj);

    void call(@NotNull T event) throws Throwable;

    String describe();

    @Override
    default int compareTo(@NotNull Listener<?> o) {
        return Integer.compare(getPriority(), o.getPriority());
    }
}
