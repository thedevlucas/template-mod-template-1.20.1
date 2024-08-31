package com.example.dialogue;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class ObjectiveWindow implements IDialogueWindow {
    private final MinecraftClient client;
    private final long startTime;
    private final String objective;

    public ObjectiveWindow(MinecraftClient client, String objective) {
        this.client = client;
        this.objective = objective;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        int screenWidth = client.getWindow().getScaledWidth();
        MatrixStack matrix = context.getMatrices();
        matrix.push();


        float timeActive = (System.currentTimeMillis() - startTime) / 1000.0F;
        float SCALE_TIME = 0.5F;

        float scaleProg = MathHelper.clamp(1.0F - (timeActive / SCALE_TIME), 0.0F, 1.0F);
        float easedScaleProg = easeInOutCubic(scaleProg);
        float scale = 1.0F + 1.5F * easedScaleProg;

        matrix.translate(screenWidth / 2.0F, 10.0F, 0.0F);
        matrix.scale(scale, scale, 1.0F);

        MutableText displayText = Text.translatable("Objective: ").append(Text.literal(objective).setStyle(Style.EMPTY.withColor(8387839)));
        int textWidth = client.textRenderer.getWidth(displayText);

        int xPosition = -textWidth / 2;
        int yPosition = 0;
        context.drawTextWithShadow(client.textRenderer, displayText, xPosition, yPosition, 0xFFFFFFFF);

        matrix.pop();
    }

    private float easeInOutCubic(float t) {
        return t < 0.5 ? 2 * t * t : 1 - (float)Math.pow(-2 * t + 2, 2) / 2;
    }

    @Override
    public boolean isDone() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - startTime >= 5500);
    }
}
