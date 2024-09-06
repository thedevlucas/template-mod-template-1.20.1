package com.example.entity;

import com.example.TemplateMod;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderType;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;

import java.awt.*;
import static team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry.*;

public class SphereEntityRenderer extends EntityRenderer<SphereEntity> {

    private static RenderTypeToken getRenderTypeToken() {
        return RenderTypeToken.createToken(new Identifier(TemplateMod.MOD_ID, "textures/vfx/noise.png"));
    }

    private static final LodestoneRenderType RENDER_LAYER = ADDITIVE_TEXTURE.applyAndCache(getRenderTypeToken());


    public SphereEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(SphereEntity entity) {
        return null;
    }

    @Override
    public void render(SphereEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        VFXBuilders.createWorld().setColor(new Color(100,200,200)).setAlpha(1F).setRenderType(RENDER_LAYER).renderSphere(matrices, 10,20,20);
    }
}
