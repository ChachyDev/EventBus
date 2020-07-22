package dev.deamsy.eventbus.impl;

import dev.deamsy.eventbus.api.EventBus;
import dev.deamsy.eventbus.api.listener.LambdaEventListener;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractEventBus implements EventBus {
    @Override
    public int[] register(@NotNull Object object) {
        return register(castClassToObjectClass(object.getClass()), object);
    }

    @Override
    public int[] register(@NotNull Class<@NotNull ?> clazz) {
        return register(castClassToObjectClass(clazz), null);
    }

    @Override
    public <@NotNull T> int registerLambda(Class<@NotNull T> eventClass, LambdaEventListener<@NotNull T> listener) {
        return registerLambda(eventClass, listener, 0);
    }

    @Override
    public void unregister(Object object) {
        unregister(castClassToObjectClass(object.getClass()), object);
    }

    @Override
    public void unregister(Class<@NotNull ?> clazz) {
        unregister(clazz, null);
    }

    @SuppressWarnings("unchecked")
    private Class<Object> castClassToObjectClass(Class<@NotNull ?> c) {
        return (Class<@NotNull Object>) c;
    }
}
