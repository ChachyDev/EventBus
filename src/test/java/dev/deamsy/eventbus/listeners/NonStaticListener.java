package dev.deamsy.eventbus.listeners;

import dev.deamsy.eventbus.api.listener.EventListener;
import dev.deamsy.eventbus.events.DataEvent;

public class NonStaticListener {
    @EventListener
    public void onEvent(DataEvent event) {
        event.currentData = "Success";
    }
}
