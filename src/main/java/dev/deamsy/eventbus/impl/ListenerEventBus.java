package dev.deamsy.eventbus.impl;

import dev.deamsy.eventbus.api.listener.EventListener;
import dev.deamsy.eventbus.api.listener.LambdaEventListener;
import dev.deamsy.eventbus.impl.listener.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public abstract class ListenerEventBus extends AbstractEventBus {
    private final AtomicInteger currentListenerId = new AtomicInteger(0);
    protected Map<Integer, Listener<?>> listeners = new LinkedHashMap<>();

    @Override
    public <@NotNull T> int registerLambda(@NotNull Class<T> eventClass, LambdaEventListener<T> listener, int priority) {
        return registerListener(new Listener<T>() {
            @Override
            public @NotNull Class<T> getEventClass() {
                return eventClass;
            }

            @Override
            public boolean matches(@NotNull Class<?> owner, @Nullable Object obj) {
                return obj == listener;
            }

            @Override
            public int getPriority() {
                return priority;
            }

            @Override
            public void call(@NotNull T event) throws Throwable {
                listener.call(event);
            }

            @Override
            public String describe() {
                return String.format("<lambda listener, backing implementation %s>", listener);
            }
        });
    }

    @Override
    public <@NotNull T> void unregister(@NotNull Class<@NotNull T> clazz, @Nullable T object) {
        listeners.entrySet().removeIf(entry -> entry.getValue().matches(clazz, object));
    }

    @Override
    public void unregister(int id) {
        listeners.remove(id);
    }

    protected int registerListener(@NotNull Listener<?> listener) {
        int id = generateNewId();
        listeners.put(id, listener);
        sortListenerMap();
        return id;
    }

    @Override
    public <T> int[] register(@NotNull Class<T> clazz, @Nullable T object) {
        List<Method> validEventListenerMethods = new ArrayList<>();

        for (Method method : clazz.getMethods()) {
            // method.setAccessible(true); // speed up reflection access
            if (method.getAnnotation(EventListener.class) != null && method.getParameterCount() == 1) {
                if (!Modifier.isStatic(method.getModifiers()) && object == null) {
                    continue;
                }
                validEventListenerMethods.add(method);
            }
        }

        int[] ids = new int[validEventListenerMethods.size()];

        for (int i = 0; i < validEventListenerMethods.size(); i++) {
            int id = generateNewId();
            listeners.put(id, createListener(validEventListenerMethods.get(i), object, id));
            ids[i] = id;
        }

        sortListenerMap();

        return ids;
    }

    @Override
    public <T> @NotNull T post(@NotNull T event) {
        for (Listener<?> listener : listeners.values()) {
            if (listener.getEventClass().isAssignableFrom(event.getClass())) {
                try {
                    //noinspection unchecked
                    ((Listener<Object>) listener).call(event);
                } catch (Throwable e) {
                    throw new IllegalStateException(String.format("Listener %s failed to process event %s", listener.describe(), event), e);
                }
            }
        }

        return event;
    }

    protected int generateNewId() {
        return currentListenerId.getAndIncrement();
    }

    protected void sortListenerMap() {
        listeners = listeners.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    protected abstract Listener<?> createListener(Method method, Object obj, int id);
}
