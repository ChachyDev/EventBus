package dev.deamsy.eventbus.listeners;

import dev.deamsy.eventbus.api.listener.EventListener;
import dev.deamsy.eventbus.events.DataEvent;

public class InvertedMixedStaticListener {
    @EventListener
    public void nonStatic(DataEvent event) {
        event.currentData = "Not static";
    }

    @EventListener(10)
    public static void yesStatic(DataEvent event) {
        event.currentData = "Static";
    }
}
