package org.lud.engine.service;

import org.lud.engine.interfaces.Service;

import java.util.LinkedHashMap;
import java.util.Map;

public class ServiceFactory {
    private final Map<Class<? extends Service>, Service> services = new LinkedHashMap<>();

    public <T extends Service> void register(Class<T> type, T instance) {
        services.put(type, instance);
    }

    public <T extends Service> T get(Class<T> type) {
        return type.cast(services.get(type));
    }
}
