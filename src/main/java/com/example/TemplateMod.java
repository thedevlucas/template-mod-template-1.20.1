package com.example;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateMod implements ModInitializer {
	public static final String MOD_ID = "template-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> server.getGameRules().get(GameRules.DO_IMMEDIATE_RESPAWN).set(true,server)));

		LOGGER.info(MOD_ID + "Server side loaded successfully.");
	}
}