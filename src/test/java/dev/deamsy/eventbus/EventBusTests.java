package dev.deamsy.eventbus;

import dev.deamsy.eventbus.api.EventBus;
import dev.deamsy.eventbus.api.event.ICancellableEvent;
import dev.deamsy.eventbus.api.listener.LambdaEventListener;
import dev.deamsy.eventbus.events.AnotherDataEvent;
import dev.deamsy.eventbus.events.DataEvent;
import dev.deamsy.eventbus.events.TestCancellableEvent;
import dev.deamsy.eventbus.listeners.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class EventBusTests {
    protected abstract EventBus createEventBus();

    @Test
    public void testReceiveSubclass() {
        EventBus bus = createEventBus();
        bus.registerLambda(DataEvent.class, event -> event.currentData = "Success");
        AnotherDataEvent event = new AnotherDataEvent();
        assertNull(event.currentData);
        bus.post(event);
        assertEquals("Success", event.currentData);
    }

    @Test
    public void testPostStatic() {
        EventBus bus = createEventBus();
        bus.register(StaticListener.class);
        DataEvent event = new DataEvent();
        assertNull(event.currentData);
        bus.post(event);
        assertEquals("Success", event.currentData);
    }

    @Test
    public void testPost() {
        EventBus bus = createEventBus();
        bus.register(StaticListener.class);
        DataEvent event = new DataEvent();
        assertNull(event.currentData);
        DataEvent result = bus.post(event);
        assertEquals(event, result);
        assertEquals("Success", event.currentData);
    }

    @Test
    public void testPostNonStatic() {
        EventBus bus = createEventBus();
        bus.register(NonStaticListener.class);
        DataEvent event = new DataEvent();
        assertNull(event.currentData);
        bus.post(event);
        assertNull(event.currentData);
        bus.register(new NonStaticListener());
        assertEquals("Success", bus.post(event).currentData);
    }

    @Test
    public void testPostPriority() {
        EventBus bus = createEventBus();
        bus.register(PriorityListener.class);
        DataEvent event = new DataEvent();
        assertEquals(10, bus.post(event).currentData);
    }

    @Test
    public void testPriorityLambda() {
        EventBus bus = createEventBus();
        bus.registerLambda(DataEvent.class, event -> event.currentData = 10, 10);
        bus.registerLambda(DataEvent.class, event -> event.currentData = 0);
        assertEquals(10, bus.post(new DataEvent()).currentData);
    }

    @Test
    public void testPostMixedStatic() {
        EventBus bus = createEventBus();
        bus.register(MixedStaticListener.class);
        DataEvent event = new DataEvent();
        assertNull(event.currentData);
        assertEquals("Static", bus.post(event).currentData);
        bus.unregister(MixedStaticListener.class);
        bus.register(new MixedStaticListener());
        assertEquals("Not static", bus.post(event).currentData);
    }

    @Test
    public void testUnregisterMethod() {
        EventBus bus = createEventBus();
        NonStaticListener listener = new NonStaticListener();
        bus.register(listener);
        assertEquals("Success", bus.post(new DataEvent()).currentData);
        bus.unregister(listener);
        assertNull(bus.post(new DataEvent()).currentData);
    }

    @Test
    public void testUnregisterMethodById() {
        EventBus bus = createEventBus();
        int[] ids = bus.register(new NonStaticListener());
        assertEquals("Success", bus.post(new DataEvent()).currentData);
        bus.unregister(ids);
        assertNull(bus.post(new DataEvent()).currentData);
    }

    @Test
    public void testUnregisterLambda() {
        EventBus bus = createEventBus();
        LambdaEventListener<DataEvent> listener = event -> event.currentData = "Success";
        bus.registerLambda(DataEvent.class, listener);
        assertEquals("Success", bus.post(new DataEvent()).currentData);
        bus.unregister(listener);
        assertNull(bus.post(new DataEvent()).currentData);
    }

    @Test
    public void testUnregisterLambdaById() {
        EventBus bus = createEventBus();
        int id = bus.registerLambda(DataEvent.class, event -> event.currentData = "Success");
        assertEquals("Success", bus.post(new DataEvent()).currentData);
        bus.unregister(id);
        assertNull(bus.post(new DataEvent()).currentData);
    }

    @Test
    public void testThrowMethod() {
        EventBus bus = createEventBus();
        assertDoesNotThrow(() -> bus.post(new DataEvent()));
        bus.register(ThrowingListener.class);
        assertThrows(IllegalStateException.class, () -> bus.post(new DataEvent()));
    }

    @Test
    public void testThrowLambda() {
        EventBus bus = createEventBus();
        assertDoesNotThrow(() -> bus.post(new DataEvent()));
        bus.registerLambda(DataEvent.class, event -> {
            throw new RuntimeException();
        });
        assertThrows(IllegalStateException.class, () -> bus.post(new DataEvent()));
    }

    @Test
    public void testCancellableEvent() {
        EventBus bus = createEventBus();
        assertFalse(bus.post(new TestCancellableEvent()).isCancelled());
        int id = bus.registerLambda(TestCancellableEvent.class, event -> event.setCancelled(true));
        assertTrue(bus.post(new TestCancellableEvent()).isCancelled());
        bus.unregister(id);
        bus.registerLambda(TestCancellableEvent.class, ICancellableEvent::cancel);
        assertTrue(bus.post(new TestCancellableEvent()).isCancelled());
        bus.registerLambda(TestCancellableEvent.class, event -> event.setCancelled(false), 1);
        assertFalse(bus.post(new TestCancellableEvent()).isCancelled());
    }
}
