package com.example.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.NbtCompound;

public class FloatingTextEntity extends Entity {

    public FloatingTextEntity(EntityType<? extends Entity> entityType, World world) {
        super(entityType, world);
    }

    // Métodos necesarios para la entidad
    @Override
    protected void initDataTracker() {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}

    @Override
    public void tick() {
        super.tick();
        // Lógica adicional si es necesario
    }

    // Método para configurar la posición de la entidad
    public void setPosition(BlockPos pos) {
        this.setPos(pos.getX(), pos.getY(), pos.getZ());
    }
}
