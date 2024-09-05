package com.example.dialogue;

import com.example.TemplateMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class ObjectiveFailed implements IDialogueWindow {
    private final MinecraftClient client;
    private final long startTime;
    private final Identifier failed_bar = new Identifier(TemplateMod.MOD_ID, "textures/gui/objective_status/failed_mission_bar.png");
    private float progress = 0.0F;

    public ObjectiveFailed(MinecraftClient client) {
        this.client = client;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        int screenWidth = client.getWindow().getScaledWidth();
        MatrixStack matrix = context.getMatrices();

        float timeActive = (System.currentTimeMillis() - startTime) / 1000.0F;
        float SCALE_TIME = 1.5F;

        if (timeActive >= 8) {
            progress = MathHelper.clamp(1.0F - (timeActive - 8.0F) / SCALE_TIME, 0.0F, 1.0F);
        } else {
            progress = MathHelper.clamp(timeActive / 3.0F, 0.0F, 1.0F);
        }

        float easedProgress = easeInOutElastic(progress);

        int width = 160;
        int x = screenWidth - width - 20;

        int initialX = screenWidth + 200;
        int targetX = x + width + 20;
        int currentX = initialX + (int)((targetX - initialX) * easedProgress);
        int descriptionX = x + width - client.textRenderer.getWidth("ᴍɪssɪᴏɴ ғᴀɪɪʟᴇᴅ");

        matrix.push();
        matrix.translate(currentX - x - width - 10, 0, 0);

        context.drawTexture(failed_bar, x, 50, 0, 0, width, 6, width, 6);
        context.drawTextWithShadow(client.textRenderer, "ᴍɪssɪᴏɴ ғᴀɪʟᴇᴅ", descriptionX, 35, 0xFFFFFFFF);

        matrix.pop();
    }

    private float easeInOutElastic(float t) {
        float c5 = (2 * (float)Math.PI) / 4.5f;
        return t == 0 ? 0 : t == 1 ? 1 : t < 0.5 ? -(float)Math.pow(2, 20 * t - 10) * (float)Math.sin((20 * t - 11.125) * c5) / 2 : (float)Math.pow(2, -20 * t + 10) * (float)Math.sin((20 * t - 11.125) * c5) / 2 + 1;
    }

    @Override
    public boolean isDone() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - startTime >= 10000);
    }
}
