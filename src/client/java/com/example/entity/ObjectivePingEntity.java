package com.example.particles;

import com.example.entity.EntityTypeModule;
import com.example.particles.contracts.ColoredParticleInitialData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class ObjectivePingEntity extends Entity {
    private static final TrackedData<Integer> COLOR = DataTracker.registerData(ObjectivePingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private UUID objectiveUUID;

    public ObjectivePingEntity(EntityType<? extends ObjectivePingEntity> entityType, World level) {
        super(entityType, level);
    }


    protected void initDataTracker() {
        this.dataTracker.startTracking(COLOR, Integer.valueOf(16711680));
    }

    public int getColor() {
        return ((Integer)this.dataTracker.get(COLOR)).intValue();
    }

    public void setColor(int color) {
        this.dataTracker.set(COLOR, Integer.valueOf(color));
    }

    protected void readCustomDataFromNbt(NbtCompound compound) {
        setColor(compound.getInt("color"));
        this.objectiveUUID = compound.getUuid("objectiveUUID");
    }

    protected void writeCustomDataToNbt(NbtCompound compound) {
        compound.putInt("color", getColor());
        compound.putUuid("objectiveUUID", this.objectiveUUID);
    }

    public boolean isCollidable() {
        return false;
    }

    public boolean collidesWithStateAtPos(BlockPos pos, BlockState state) {
        return false;
    }

    public boolean canHit() {
        return false;
    }

    public void tick() {
        super.tick();

        if (!this.getWorld().isClient) {
            discard();
        }

        if (this.getWorld().isClient ) {
            ColoredParticleInitialData data = new ColoredParticleInitialData(getColor());
            this.getWorld().addParticle(ParticleRegister.PING.setData(data), true, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
        }
    }
}
