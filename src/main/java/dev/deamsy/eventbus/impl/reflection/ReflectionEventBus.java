package dev.deamsy.eventbus.impl.reflection;

import dev.deamsy.eventbus.api.listener.EventListener;
import dev.deamsy.eventbus.impl.ListenerEventBus;
import dev.deamsy.eventbus.impl.listener.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionEventBus extends ListenerEventBus {
    protected @NotNull Listener<?> createListener(@NotNull Method method, @Nullable Object obj, int id) {
        return new ReflectionEventListener(
                method.getParameterTypes()[0],
                method,
                Modifier.isStatic(method.getModifiers()) ? null : obj, // we could pass object here if it's static, but that might break matches()
                method.getAnnotation(EventListener.class).value()
        );
    }
}
