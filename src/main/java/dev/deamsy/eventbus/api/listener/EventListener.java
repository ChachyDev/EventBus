package dev.deamsy.eventbus.api.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation for event listener methods.
 *
 * @author deamsy
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventListener {
    /**
     * @return the priority of this event listener, with {@link Integer#MAX_VALUE} being executed first and
     * {@link Integer#MIN_VALUE} last.
     */
    int value() default 0;
}
