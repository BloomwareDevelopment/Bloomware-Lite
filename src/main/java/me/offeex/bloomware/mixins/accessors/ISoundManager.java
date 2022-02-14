package me.offeex.bloomware.mixins.accessors;

import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SoundManager.class)
public interface ISoundManager {
    @Accessor("soundSystem")
    SoundSystem getSoundSystem();
}
