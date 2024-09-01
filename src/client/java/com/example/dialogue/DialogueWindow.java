package com.example.dialogue;

import com.example.TemplateMod;
import com.example.Utilities;
import com.example.sound.ModSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

import java.util.List;

public class DialogueWindow implements IDialogueWindow {
    private final MinecraftClient client;
    private final String text;
    private final int duration;
    private final int typingSpeed;
    private long lastUpdateTime;
    private int currentIndex;
    private boolean textFullyDisplayed;
    private final Identifier texture = new Identifier(TemplateMod.MOD_ID, "textures/gui/dialogue_frame.png");

    public DialogueWindow(MinecraftClient client, String text, int duration, int typingSpeed) {
        this.client = client;
        this.text = text;
        this.duration = duration;
        this.typingSpeed = typingSpeed;
        this.lastUpdateTime = System.currentTimeMillis();
        this.currentIndex = 0;
        this.textFullyDisplayed = false;
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        TextRenderer textRenderer = client.textRenderer;
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        PlayerEntity plr = MinecraftClient.getInstance().player;

        int height = 240;
        int width = 450;
        int x = (screenWidth - width) / 2;
        int y = screenHeight - height - 60;

        Utilities.toggleRender(false, client);

        context.drawTexture(texture, x, y + 60,0,0, width, height, width, height);

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime >= typingSpeed) {
            if (currentIndex < text.length()) {
                currentIndex++;
                lastUpdateTime = currentTime;

                // Reproducir sonido cada vez que se añade una nueva letra
                if (plr != null){
                    plr.playSound(ModSounds.TEXT, SoundCategory.PLAYERS, 0.6f, 1f);
                }
            }
            if (currentIndex >= text.length() && !textFullyDisplayed) {
                textFullyDisplayed = true;
            }
        }

        String displayedText = text.substring(0, Math.min(currentIndex, text.length()));
        List<String> lines = Utilities.wrapText(textRenderer, displayedText, width - 120);

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            context.drawText(textRenderer, line, x + 60, y + height - 10 + i * textRenderer.fontHeight, Utilities.rgba(255, 255, 255, 1f), true);
        }

    }

    @Override
    public boolean isDone() {
        long currentTime = System.currentTimeMillis();
        return textFullyDisplayed && (currentTime - lastUpdateTime >= duration * 1000L);
    }
}
