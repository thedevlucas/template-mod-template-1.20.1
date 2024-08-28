package com.example.particles;

import com.example.TemplateMod;
import com.example.particles.types.PingParticleType;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

import java.util.function.BiConsumer;


public interface ParticleRegister {

    public static final PingParticleType PING = new PingParticleType(true);

    static void init() {
        initParticles(bind(Registries.PARTICLE_TYPE));
    }

    static void registerFactories() {
        ParticleFactoryRegistry.getInstance().register((ParticleType) PING, LodestoneWorldParticleType.Factory::new);
    }

    private static void initParticles(BiConsumer<ParticleType<?>, Identifier> registry){
        registry.accept(PING, new Identifier(TemplateMod.MOD_ID, "waypoint"));
    }

    private static <T> BiConsumer<T, Identifier> bind(Registry<? super T> registry) {
        return (t, id) -> Registry.register(registry, id, t);
    }



}
