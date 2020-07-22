package dev.deamsy.eventbus.implementations;

import dev.deamsy.eventbus.EventBusTests;
import dev.deamsy.eventbus.api.EventBus;
import dev.deamsy.eventbus.impl.reflection.ReflectionEventBus;

public class ReflectionEventBusTests extends EventBusTests {
    @Override
    protected EventBus createEventBus() {
        return new ReflectionEventBus();
    }
}
