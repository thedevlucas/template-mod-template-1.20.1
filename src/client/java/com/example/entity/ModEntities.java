package com.example.entity;

import com.example.TemplateMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<FloatingTextEntity> FLOATING_TEXT_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TemplateMod.MOD_ID, "floating_text"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, FloatingTextEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0F, 0.5F))
                    .build()
    );
}
