package com.example.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

public class FloatingTextEntityRenderer extends EntityRenderer<FloatingTextEntity> {

    public FloatingTextEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(FloatingTextEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

        matrices.push();
        matrices.translate(0.5, 2.0, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
        matrices.scale(0.02F, 0.02F, 0.02F);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        String text = entity.getText();

        textRenderer.draw(text, -textRenderer.getWidth(text) / 2f, 0, 0xFFFFFF, false, matrix, immediate, TextRenderer.TextLayerType.NORMAL, 0, 15728880);
        matrices.pop();
    }

    @Override
    public Identifier getTexture(FloatingTextEntity entity) {
        return null; // No texture needed
    }
}
