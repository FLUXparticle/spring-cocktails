package org.example.bars.service;

import org.example.bars.entry.*;
import org.springframework.beans.*;
import org.springframework.stereotype.*;

/**
 * Keeps the exercise UI working before the optional version property is added.
 */
@Component("barVersions")
public class BarVersions {
    public static Long get(Bar bar) {
        var properties = new BeanWrapperImpl(bar);
        return properties.isReadableProperty("version") ? (Long) properties.getPropertyValue("version") : null;
    }

    public static void set(Bar bar, Long version) {
        var properties = new BeanWrapperImpl(bar);
        if (properties.isWritableProperty("version")) properties.setPropertyValue("version", version);
    }
}
