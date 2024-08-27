package com.example;

import net.fabricmc.api.ModInitializer;


import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TemplateMod implements ModInitializer {
	public static final String MOD_ID = "template-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
			server.getGameRules().get(GameRules.DO_IMMEDIATE_RESPAWN).set(true,server);
		}));



		LOGGER.info("Hello Fabric world!");
	}
}