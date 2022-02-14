package me.offeex.bloomware.client.setting;

import java.util.ArrayList;
import java.util.List;

public class SettingUpdateBus {
    private final List<Handler> handlers = new ArrayList<>();

    public void subscribe(Handler handler) {
        this.handlers.add(handler);
    }

    public void unSubscribe(Handler handler) {
        this.handlers.remove(handler);
    }

    public void trigger() {
        for (Handler handler : this.handlers)
            handler.method();
    }

    @FunctionalInterface
    public interface Handler {
        void method();
    }
}
