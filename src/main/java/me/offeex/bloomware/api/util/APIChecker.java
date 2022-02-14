package me.offeex.bloomware.api.util;

import me.offeex.bloomware.Bloomware;
import net.fabricmc.loader.api.FabricLoader;

public class APIChecker {
    public APIChecker() {
        if (!FabricLoader.getInstance().isModLoaded("fabric")) {
            Bloomware.logger.info("Seems you have not installed Fabric-Api.");
            Bloomware.logger.info("Drag Fabric-Api into /mods folder and try again.");
            Runtime.getRuntime().exit(1);
        }
    }
}
