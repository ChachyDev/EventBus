package dev.deamsy.eventbus.impl.asm;

import dev.deamsy.eventbus.impl.listener.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

public abstract class BaseASMEventListener implements Listener<Object> {
    private final Class<?> eventClass;
    private final int priority;
    private final Method method;
    protected final Object obj;

    public BaseASMEventListener(Class<?> eventClass, int priority, Method method, Object obj) {
        this.eventClass = eventClass;
        this.priority = priority;
        this.method = method;
        this.obj = obj;
    }

    @Override
    public @NotNull Class<?> getEventClass() {
        return eventClass;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean matches(@NotNull Class<?> owner, @Nullable Object obj) {
        if (this.obj != null) {
            return this.obj == obj; // intentionally use == here instead of equals() as we want to check if the objects are the same, not if they're equal
        }
        return owner.equals(method.getDeclaringClass());
    }

    @Override
    public String describe() {
        return method.getDeclaringClass().getName() + "." + method.getName() + "(" + eventClass.getName() + ")";
    }
}
