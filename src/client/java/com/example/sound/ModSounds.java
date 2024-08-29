package com.example.sound;

import com.example.TemplateMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds  {
    public static final SoundEvent TELEPORT_SOUND_EFFECT = registerSoundEvent("teleport_sound_effect");
    public static final SoundEvent OBJECTIVE_SOUND_EFFECT = registerSoundEvent("objective_sound_effect");
    public static final SoundEvent BLACK_HOLE = registerSoundEvent("black_hole");
    public static final SoundEvent TEXT = registerSoundEvent("text");


    private static SoundEvent registerSoundEvent(String location) {
        SoundEvent soundEvent = SoundEvent.of(new Identifier(TemplateMod.MOD_ID, location));
        return Registry.register(Registries.SOUND_EVENT, soundEvent.getId(), soundEvent);
    }

    public static void register() {}
}
