package dev.deamsy.eventbus.impl.reflection;

import dev.deamsy.eventbus.impl.listener.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionEventListener implements Listener<Object> {
    private final Class<?> eventClass;
    private final Method method;
    private final Object obj;
    private final int priority;

    public ReflectionEventListener(Class<?> eventClass, Method method, Object obj, int priority) {
        this.eventClass = eventClass;
        this.method = method;
        this.obj = obj;
        this.priority = priority;
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
    public void call(@NotNull Object event) throws Throwable {
        try {
            method.invoke(obj, event);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    @Override
    public String describe() {
        return method.getDeclaringClass().getName() + "." + method.getName() + "(" + eventClass.getName() + ")";
    }
}
