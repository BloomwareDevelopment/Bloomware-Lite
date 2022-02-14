package me.offeex.bloomware.client.setting;

import java.util.ArrayList;
import java.util.List;

public class SettingValueUpdateBus<T> {
    private final List<Handler<T>> handlers = new ArrayList<>();

    public void subscribe(Handler<T> handler) {
        this.handlers.add(handler);
    }

    public void unSubscribe(Handler<T> handler) {
        this.handlers.remove(handler);
    }

    public void trigger(T oldValue, T newValue) {
        for (Handler<T> handler : this.handlers)
            handler.method(oldValue, newValue);
    }

    @FunctionalInterface
    public interface Handler<T> {
        void method(T oldValue, T newValue);
    }
}
