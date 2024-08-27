package com.example.packets;

import com.example.TemplateMod;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;


public class ModPackets {
    public static final Identifier PARTICLE_SPAWN_ID = new Identifier(TemplateMod.MOD_ID, "particle_spawn");

    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(PARTICLE_SPAWN_ID, (server, player, handler, buf, responseSender) -> {
            ParticlePacket packet = new ParticlePacket(buf);
            server.execute(() -> {
                // Server-side handling if needed
            });
        });
    }
}