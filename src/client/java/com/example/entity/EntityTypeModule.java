package com.example.entity;

import com.example.TemplateMod;
import com.example.particles.ObjectivePingEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityTypeModule {
    public static final EntityType<ObjectivePingEntity> OBJECTIVE_PING = registerEntityType("objective_ping",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ObjectivePingEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6F, 1.8F)) // Cambiado a un tamaño más razonable
                    .trackRangeChunks(128)
                    .build());

    private static <T extends Entity> EntityType<T> registerEntityType(String name, EntityType<T> entityType) {
        return Registry.register(Registries.ENTITY_TYPE, new Identifier(TemplateMod.MOD_ID, name), entityType);
    }

    public static void register() {
        // Aquí se pueden realizar otras registraciones necesarias.
    }
}
