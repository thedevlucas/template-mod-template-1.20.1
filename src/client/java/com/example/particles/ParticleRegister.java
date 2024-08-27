package com.example.particles;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;

import net.minecraft.util.Identifier;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

import java.util.function.BiConsumer;


public class ParticleRegister {
    public static LazyRegistrar<ParticleType<?>> PARTICLES = LazyRegistrar.create(Registries.PARTICLE_TYPE, LodestoneLib.LODESTONE);

    public static final RegistryObject<LodestoneWorldParticleType> TEST_PARTICLE = PARTICLES.register("waypoint", LodestoneWorldParticleType::new);

    public static void registerFactories() {
        ParticleFactoryRegistry.getInstance().register(TEST_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
    }


}
