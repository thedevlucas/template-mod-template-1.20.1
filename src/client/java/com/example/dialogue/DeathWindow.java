package com.example.dialogue;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

public class DeathWindow implements IDialogueWindow {
    private final MinecraftClient client;
    private final long lastUpdateTime;
    private float totalTickDelta;

    public DeathWindow(MinecraftClient client) {
        this.client = client;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int color = 0x80FF0000;
        int targetColor = 0x00FF0000;

        totalTickDelta += tickDelta;

        float lerpedAmount = MathHelper.abs(MathHelper.sin(totalTickDelta / 50F));
        int lerpedColor = ColorHelper.Argb.lerp(lerpedAmount, color, targetColor);

        context.fill(0, 0, screenWidth, screenHeight, 0, lerpedColor);
    }

    @Override
    public boolean isDone() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastUpdateTime >= 500);
    }
}
