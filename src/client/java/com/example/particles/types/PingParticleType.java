package com.example.particles.types;

import com.example.particles.contracts.ColoredParticleInitialData;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;

public class PingParticleType extends DefaultParticleType {
    public PingParticleType(boolean alwaysShow){
        super(alwaysShow);
    }
    public ColoredParticleInitialData initialData;
    public ParticleEffect setData(ColoredParticleInitialData target){
        this.initialData = target;
        return this;
    }
}
