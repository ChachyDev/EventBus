package dev.deamsy.eventbus.api.listener;

import org.jetbrains.annotations.NotNull;

public interface LambdaEventListener<T> {
    void call(@NotNull T event) throws Throwable;
}
