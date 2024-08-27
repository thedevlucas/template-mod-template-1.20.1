package com.example.dialogue;

import com.example.Utilities;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class DialogueWindow implements IDialogueWindow {
    private final MinecraftClient client;
    private final String text;
    private final int duration;
    private final int typingSpeed;
    private long lastUpdateTime;
    private int currentIndex;
    private boolean textFullyDisplayed;

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

        int y = (screenHeight - 60);

        context.fill(0, y, screenWidth, y + 60, Utilities.rgba(0, 0, 0, 0.5f));

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime >= typingSpeed) {
            if (currentIndex < text.length()) {
                currentIndex++;
                lastUpdateTime = currentTime;
            }
            if (currentIndex >= text.length() && !textFullyDisplayed) {
                textFullyDisplayed = true;
            }
        }

        String displayedText = text.substring(0, Math.min(currentIndex, text.length()));

        List<String> lines = Utilities.wrapText(textRenderer, displayedText, screenWidth);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            context.drawText(textRenderer, line, 10, y + 10 + i * textRenderer.fontHeight, Utilities.rgba(255, 255, 255, 1f), true);
        }
    }

    @Override
    public boolean isDone() {
        long currentTime = System.currentTimeMillis();
        return textFullyDisplayed && (currentTime - lastUpdateTime >= duration * 1000);
    }
}
