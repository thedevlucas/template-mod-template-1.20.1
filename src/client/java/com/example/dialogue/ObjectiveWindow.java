package com.example.dialogue;

import com.example.TemplateMod;

import com.example.TemplateModClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class ObjectiveWindow implements IDialogueWindow {
    private final MinecraftClient client;
    private final long startTime;
    private final String objective;
    private final Identifier texture = new Identifier(TemplateMod.MOD_ID, "textures/gui/objective_status/norml_and_current_mission_bar.png");
    private float progress = 0.0F;

    public ObjectiveWindow(MinecraftClient client, String objective) {
        this.client = client;
        this.objective = objective;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        int screenWidth = client.getWindow().getScaledWidth();
        MatrixStack matrix = context.getMatrices();

        float timeActive = (System.currentTimeMillis() - startTime) / 1000.0F;
        float SCALE_TIME = 2F;


        if (timeActive >= 8) {
            progress = MathHelper.clamp(1.0F - (timeActive - 8.0F) / SCALE_TIME, 0.0F, 1.0F);
        } else {
            progress = MathHelper.clamp(timeActive / 3.0F, 0.0F, 1.0F);
        }

        float easedProgress = easeInOutElastic(progress);

        int width = 160;
        int x = screenWidth - width - 20;

        int initialX = screenWidth + 200;
        int targetX = x + width + 10;
        int currentX = initialX + (int)((targetX - initialX) * easedProgress);
        int descriptionX = x + width - client.textRenderer.getWidth(this.objective);

        matrix.push();
        matrix.translate(currentX - x - width - 10, 0, 0);

        context.drawTexture(texture, x, 50, 0, 0, width, 6, width, 10);
        context.drawTextWithShadow(client.textRenderer, "ɴᴇᴡ ᴍɪssɪᴏɴ", x + width - 60, 35, 0xFFFFFFFF);
        context.drawTextWithShadow(client.textRenderer, this.objective, descriptionX, 60, 0xFFFFFFFF);

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
