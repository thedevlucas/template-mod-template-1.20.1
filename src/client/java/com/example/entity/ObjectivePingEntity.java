package com.example.entity;

import com.example.particles.ParticleRegister;
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

    public ObjectivePingEntity(EntityType<? extends ObjectivePingEntity> entityType, World world) {
        super(entityType, world);
    }

    public void initialize(double x, double y, double z, int color, UUID uuid) {
        setPosition(x, y, z);
        setColor(color);
        this.objectiveUUID = uuid;
    }

    protected void initDataTracker() {
        this.dataTracker.startTracking(COLOR, 16711680);
    }

    public int getColor() {
        return this.dataTracker.get(COLOR);
    }

    public void setColor(int color) {
        this.dataTracker.set(COLOR, color);
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
