package com.example.shaders.post;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class CoreEntity extends Entity {
    private static final TrackedData<Integer> BREAKING_TICKS = DataTracker.registerData(CoreEntity.class, TrackedDataHandlerRegistry.INTEGER);


    public CoreEntity(EntityType<?> entityType, World level) {
        super(entityType, level);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(BREAKING_TICKS, 0);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound compound) {
        setBreakingTicks(compound.getInt("breakingTicks"));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound compound) {
        compound.putInt("breakingTicks", getBreakingTicks());
    }

    public int getBreakingTicks() {
        return this.dataTracker.get(BREAKING_TICKS);
    }

    public void setBreakingTicks(int size) {
        this.dataTracker.set(BREAKING_TICKS, size);
    }

}
