package dev.deamsy.eventbus.implementations;

import dev.deamsy.eventbus.EventBusTests;
import dev.deamsy.eventbus.api.EventBus;
import dev.deamsy.eventbus.impl.asm.ASMEventBus;

public class ASMEventBusTests extends EventBusTests {
    @Override
    protected EventBus createEventBus() {
        return new ASMEventBus();
    }
}
