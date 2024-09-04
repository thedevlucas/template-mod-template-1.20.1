package com.example;

import com.example.dialogue.DeathWindow;
import com.example.dialogue.DialogueManager;
import com.example.entity.FloatingTextEntity;
import com.example.entity.FloatingTextEntityRenderer;
import com.example.entity.ModEntities;
import com.example.item.ModItems;
import com.example.packets.ModClientPackets;
import com.example.packets.ModPackets;
import com.example.packets.ParticlePacket;
import com.example.particles.DefaultParticleBuilderFactory;
import com.example.particles.ParticleBuilderFactory;
import com.example.sound.ModSounds;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

import static com.example.packets.ModPackets.PARTICLE_SPAWN_ID;

public class TemplateModClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("template-mod");

	@Override
	public void onInitializeClient() {
		ModPackets.registerPackets();
		ModClientPackets.registerClientPackets();
		ModSounds.register();

		//BlackHoleRenderer.init();
		ModItems.registerModItems();
		HudRenderCallback.EVENT.register(DialogueManager::renderAll);
		EntityRendererRegistry.register(ModEntities.FLOATING_TEXT_ENTITY_TYPE, FloatingTextEntityRenderer::new);


		ServerLivingEntityEvents.AFTER_DEATH.register(((entity, damageSource) -> {
			if (entity instanceof PlayerEntity){
				DialogueManager.addDialogueWindow(new DeathWindow(MinecraftClient.getInstance()));
				entity.getWorld().playSound(null, BlockPos.ofFloored(entity.getPos()), ModSounds.TELEPORT_SOUND_EFFECT, SoundCategory.PLAYERS, 1f, 1f);
			}
		}));

		UseBlockCallback.EVENT.register((plr, world, hand, hitResult) -> {
			if (!world.isClient && hand == plr.getActiveHand()) {

				//spawnNPC((ServerWorld) world, hitResult.getBlockPos());

				Color startingColor = new Color(100, 0, 100);
				Color endingColor = new Color(0, 100, 200);
				ParticleBuilderFactory factory = new DefaultParticleBuilderFactory();
				ParticlePacket packet = new ParticlePacket(hitResult.getBlockPos().toCenterPos(), startingColor.getRGB(), endingColor.getRGB(), factory);
				PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
				packet.toBytes(buf);
				ServerPlayerEntity player = (ServerPlayerEntity) plr;

                ServerPlayNetworking.send(player, PARTICLE_SPAWN_ID, buf);

				//FloatingTextEntity entity = new FloatingTextEntity(ModEntities.FLOATING_TEXT_ENTITY_TYPE, world);
				//entity.setPosition(hitResult.getBlockPos());
				//world.spawnEntity(entity);

            }
			return ActionResult.PASS;
		});

	}


}
