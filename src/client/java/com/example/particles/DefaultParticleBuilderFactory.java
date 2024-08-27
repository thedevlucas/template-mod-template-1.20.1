package com.example.particles;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;

import java.awt.*;

public class DefaultParticleBuilderFactory implements ParticleBuilderFactory{
    @Override
    public void build(World level, Vec3d pos, Color startingColor, Color endingColor) {
        WorldParticleBuilder.create(ParticleRegister.TEST_PARTICLE)
                .setScaleData(GenericParticleData.create(2f, 1f, 0f).build())
                .setTransparencyData(GenericParticleData.create(2f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.ELASTIC_IN).build())
                .setLifetime(400)
                .enableNoClip()
                .spawn(level, pos.x, pos.y + 1, pos.z);
    }
}