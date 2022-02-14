package me.offeex.bloomware.client.module;

import com.google.common.eventbus.Subscribe;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.event.events.EventKeyPress;
import me.offeex.bloomware.client.module.modules.chat.*;
import me.offeex.bloomware.client.module.modules.client.*;
import me.offeex.bloomware.client.module.modules.extension.Notifications;
import me.offeex.bloomware.client.module.modules.extension.RichPresence;
import me.offeex.bloomware.client.module.modules.extension.StreamerMode;
import me.offeex.bloomware.client.module.modules.extension.UnfocusedCPU;
import me.offeex.bloomware.client.module.modules.hud.*;
import me.offeex.bloomware.client.module.modules.interact.*;
import me.offeex.bloomware.client.module.modules.motion.*;
import me.offeex.bloomware.client.module.modules.packet.*;
import me.offeex.bloomware.client.module.modules.pvp.*;
import me.offeex.bloomware.client.module.modules.screen.*;
import me.offeex.bloomware.client.module.modules.visuals.*;
import me.offeex.bloomware.client.module.modules.world.*;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private static ArrayList<Module> modules;

    public ModuleManager() {
        modules = new ArrayList<>();
        modules.addAll(List.of(new AntiBots(), new AntiEffect(), new AntiItemLag(), new AntiParticleLag(), new AutoRespawn(), new FakePlayer(), new MobOwner(), new NewChunks(), new NoRender(), new PearlTracker(), new SoundTracker(), new Timer(),
                new BarrierView(), new BoxHighlight(), new Capes(), new Environment(), new ESP(), new FullBright(), new HoleESP(), new LogoutSpot(), new Nametags(), new StorageESP(), new Tracers(),
                new BetterScreenshot(), new CameraClip(), new CustomFOV(), new DamageTint(), new Explorer(), new FakeGamemode(), new Freecam(), new RotationLock(), new ViewModel(),
                new Anchor(), new AutoAnvil(), new AutoCrystal(), new AutoLog(), new AutoTotem(), new Criticals(), new HoleFiller(), new KillAura(), new SelfAnvil(), new SelfWeb(), new Surround(),
                new Blink(), new FakeVanillaClient(), new MultiConnect(), new PacketCancel(), new PacketLogger(), new ServerCrasher(),
                new AutoElytra(), new AutoJump(), new AutoWalk(), new BoatFly(), new ElytraFly(), new Flight(), new InventoryMove(), new NoFall(), new NoSlow(), new Speed(), new Sprint(), new Step(), new Velocity(),
                new AirPlace(), new AntiInteract(), new AutoFish(), new AutoMount(), new BuildHeight(), new FastBreak(), new FastInteract(), new MiddleClick(), new NoGhostBlock(), new Reach(), new Scaffold(), new SilentClose(), new SlotRandomizer(), new XCarry(),
                new Armor(), new BreakingBlock(), new Coordinates(), new Direction(), new Fps(), new HoleInfo(), new Hunger(), new InventoryViewer(), new LastPacket(), new MemoryUsage(), new ModuleList(), new ModuleNotifier(), new OnlineTime(), new Ping(), new PlayerCount(),
                new PlayerModel(), new PvPInfo(), new QueuePicker(), new Server(), new Speedometer(), new TabGUI(), new Target(), new TextRadar(), new Time(), new Tps(), new Watermark(), new Welcomer(), new YawPitch(),
                new Notifications(), new RichPresence(), new StreamerMode(), new UnfocusedCPU(),
                new Accounts(), new Colors(), new Font(), new Gui(), new Hud(), new MainMenu(), new Overlay(),
                new AntiSpam(), new ArmorAlert(), new AutoQueueMain(), new BetterChat(), new BetterTab(), new DeathCoords(), new Restarts2b2t(), new Spammer(), new TotemPopCounter(), new VisualRange()));
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
