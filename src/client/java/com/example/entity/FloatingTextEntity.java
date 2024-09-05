package com.example.entity;

import com.example.TemplateModClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.NbtCompound;

public class FloatingTextEntity extends Entity {

    public FloatingTextEntity(EntityType<? extends Entity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}

    @Override
    public void tick() {
        super.tick();
    }

    public void setPosition(BlockPos pos) {
        this.setPos(pos.getX(), pos.getY(), pos.getZ());
    }
}
