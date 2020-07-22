package dev.deamsy.eventbus.listeners;

import dev.deamsy.eventbus.api.listener.EventListener;
import dev.deamsy.eventbus.events.DataEvent;

public class ThrowingListener {
    @EventListener
    public static void onEvent(DataEvent event) {
        throw new RuntimeException();
    }
}
