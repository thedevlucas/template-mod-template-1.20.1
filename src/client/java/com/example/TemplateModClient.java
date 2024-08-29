package com.example;

import com.example.dialogue.DeathWindow;
import com.example.dialogue.DialogueManager;
import com.example.entity.EntityTypeModule;
import com.example.entity.ObjectivePingEntity;
import com.example.entity.ObjectivePingEntityRenderer;
import com.example.item.ModItems;
import com.example.packets.ModClientPackets;
import com.example.packets.ModPackets;
import com.example.particles.ParticleRegister;
import com.example.shaders.post.BlackHoleRenderer;
import com.example.sound.ModSounds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;

import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class TemplateModClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("template-mod");
	//public static final ScreenParticleHolder SCREEN_PARTICLES = new ScreenParticleHolder();

	@Override
	public void onInitializeClient() {
		ModPackets.registerPackets();
		ModClientPackets.registerClientPackets();
		ModSounds.register();

		ParticleRegister.registerFactories();
		ParticleRegister.init();

		EntityRendererRegistry.register(EntityTypeModule.OBJECTIVE_PING, ObjectivePingEntityRenderer::new);

		//BlackHoleRenderer.init();
		ModItems.registerModItems();
		HudRenderCallback.EVENT.register(DialogueManager::renderAll);

		MinecraftClient client = MinecraftClient.getInstance();

		ServerLivingEntityEvents.AFTER_DEATH.register(((entity, damageSource) -> {
			if (entity instanceof PlayerEntity){
				DialogueManager.addDialogueWindow(new DeathWindow(MinecraftClient.getInstance()));
				entity.getWorld().playSound(null, BlockPos.ofFloored(entity.getPos()), ModSounds.TELEPORT_SOUND_EFFECT, SoundCategory.PLAYERS, 1f, 1f);
			}
		}));


		UseBlockCallback.EVENT.register((plr, world, hand, hitResult) -> {
			if (!world.isClient && hand == plr.getActiveHand()) {

				UUID uuid = UUID.randomUUID();
				ObjectivePingEntity entity = new ObjectivePingEntity(EntityTypeModule.OBJECTIVE_PING, world);
				entity.initialize(hitResult.getBlockPos().getX(), hitResult.getBlockPos().getY(), hitResult.getBlockPos().getZ(), 16711680, uuid);
				world.spawnEntity(entity);

				LOGGER.info("hasta aca funca");
				//spawnNPC((ServerWorld) world, hitResult.getBlockPos());

				//Color startingColor = new Color(100, 0, 100);
				//Color endingColor = new Color(0, 100, 200);
				//ParticleBuilderFactory factory = new DefaultParticleBuilderFactory();
				//ParticlePacket packet = new ParticlePacket(hitResult.getBlockPos().toCenterPos(), startingColor.getRGB(), endingColor.getRGB(), factory);
				//PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
				//packet.toBytes(buf);
				//ServerPlayerEntity player = (ServerPlayerEntity) plr;

				//if (player != null) {
					//ServerPlayNetworking.send(player, PARTICLE_SPAWN_ID, buf);
					//Utilities.spawnParticles();
					//Utilities.addObjective("Hello world!", player);
					//Utilities.renderBeam(new MatrixStack(), 0,100,0);
				//}

			}
			return ActionResult.PASS;
		});
	}

	private void spawnNPC(ServerWorld world, BlockPos pos) {
		VillagerEntity villager = EntityType.VILLAGER.create(world);
		if (villager != null) {
			villager.refreshPositionAndAngles(pos, 0.0F, 0.0F);
			world.spawnEntity(villager);
		}
	}

}
