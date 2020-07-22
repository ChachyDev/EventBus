package dev.deamsy.eventbus.listeners;

import dev.deamsy.eventbus.api.listener.EventListener;
import dev.deamsy.eventbus.events.DataEvent;

public class PriorityListener {
    @EventListener(10)
    public static void highPriority(DataEvent event) {
        event.currentData = 10;
    }

    @EventListener
    public static void lowPriority(DataEvent event) {
        event.currentData = 0;
    }
}
