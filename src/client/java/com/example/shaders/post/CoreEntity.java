package com.example.shaders.post;


import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CoreEntity extends Entity {
    private static final TrackedData<Integer> BREAKING_TICKS = DataTracker.registerData(CoreEntity.class, TrackedDataHandlerRegistry.INTEGER);
    //private static final RegistryKey<World> creditsKey = RegistryKey.of(RegistryKeys.WORLD, new Identifier("template-mod", "credits"));

    public boolean renderedFirstBreakTickEffects = false;
    public ArrayList<Vec3d> cracks = new ArrayList<>();

    public CoreEntity(EntityType<?> entityType, World level) {
        super(entityType, level);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(BREAKING_TICKS, Integer.valueOf(0));
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
        return ((Integer)this.dataTracker.get(BREAKING_TICKS)).intValue();
    }

    public void setBreakingTicks(int size) {
        this.dataTracker.set(BREAKING_TICKS, Integer.valueOf(size));
    }

    public void incrementBreakingTicks() {
        this.dataTracker.set(BREAKING_TICKS, Integer.valueOf(getBreakingTicks() + 1));
    }
}
