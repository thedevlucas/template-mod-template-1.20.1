package com.example.entity;

import com.example.TemplateMod;
import com.example.TemplateModClient;
import com.example.particles.ObjectivePingEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ObjectivePingEntityRenderer extends EntityRenderer<ObjectivePingEntity> {
    private static final Identifier TEXTURE = new Identifier(TemplateMod.MOD_ID, "textures/entity/objective_ping.png");

    public ObjectivePingEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    public void render(ObjectivePingEntity objectivePingEntity, float entityYaw, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider buffer, int packedLight) {
        super.render(objectivePingEntity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    public Identifier getTexture(ObjectivePingEntity entity) {
        return TEXTURE;
    }
}
