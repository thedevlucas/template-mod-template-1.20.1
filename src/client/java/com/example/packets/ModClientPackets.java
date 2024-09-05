package com.example.packets;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

public class ModClientPackets {
    public static void registerClientPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.PARTICLE_SPAWN_ID, (client, handler, buf, responseSender) -> {
            ParticlePacket packet = new ParticlePacket(buf);
            packet.handle(MinecraftClient.getInstance());
        });
    }
}