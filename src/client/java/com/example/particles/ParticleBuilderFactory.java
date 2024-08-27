package com.example.particles;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.awt.*;

public interface ParticleBuilderFactory {
    void build(World level, Vec3d pos, Color startingColor, Color endingColor);
}