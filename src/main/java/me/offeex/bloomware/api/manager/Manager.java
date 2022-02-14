package me.offeex.bloomware.api.manager;

import me.offeex.bloomware.Bloomware;

public abstract class Manager {

    protected Manager() {
        Bloomware.EVENTBUS.register(this);
    }

    //    TODO: Singleton
}
