package dev.deamsy.eventbus.api.listener;

public interface LambdaEventListener<T> {
    void call(T event) throws Throwable;
}
