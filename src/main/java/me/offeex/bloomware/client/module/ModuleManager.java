package me.offeex.bloomware.client.module;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventKeyPress;
import me.offeex.bloomware.api.util.ClassUtil;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private static ArrayList<Module> modules;

    public ModuleManager() {
        modules = new ArrayList<>();
        try {
            ArrayList<Class<?>> classes = ClassUtil.getClassesOther("me.offeex.bloomware.client.module.modules", Module.class);
            classes.forEach(aClass -> {
                try {
                    Module m = (Module) aClass.getDeclaredConstructor().newInstance();
                    if (getModule(m.getName()) == null) modules.add(m);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
            modules.sort(Comparator.comparing(Module::getName));
        } catch (ClassNotFoundException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public Module getModule(String name) {
        return modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> module) {
        return (T) modules.stream().filter(m -> m.getClass() == module).findAny().orElse(null);
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public List<Module> getModulesByCategory(Module.Category c) {
        return modules.stream().filter(m -> m.getCategory() == c).collect(Collectors.toList());
    }

    public void onTick() {
        modules.stream().filter(Module::isEnabled).forEach(Module::onTick);
    }

    @Subscribe
    public void onKeyPress(EventKeyPress e) {
        if (InputUtil.isKeyPressed(Bloomware.mc.getWindow().getHandle(), GLFW.GLFW_KEY_F3)) return;
        modules.stream().filter(m -> m.getKey() == e.getKey()).forEach(Module::toggle);
    }
}
