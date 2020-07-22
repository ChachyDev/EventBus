package dev.deamsy.eventbus.listeners;

import dev.deamsy.eventbus.api.listener.EventListener;
import dev.deamsy.eventbus.events.DataEvent;

public class MixedStaticListener {
    @EventListener(10)
    public void nonStatic(DataEvent event) {
        event.currentData = "Not static";
    }

    @EventListener
    public static void yesStatic(DataEvent event) {
        event.currentData = "Static";
    }
}
