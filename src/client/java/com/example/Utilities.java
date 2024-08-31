package com.example;

import com.example.dialogue.DialogueManager;
import com.example.dialogue.DialogueWindow;
import com.example.dialogue.IDialogueWindow;
import com.example.dialogue.ObjectiveWindow;
import com.example.sound.ModSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

import java.util.ArrayList;
import java.util.List;

import static team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry.createGenericRenderType;

public class Utilities {
    private static boolean shouldRender = true;

    public static int rgba(int red, int green, int blue, float alpha) {
        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));

        int alphaInt = Math.round(Math.max(0, Math.min(1, alpha)) * 255);
        return (alphaInt << 24) | (red << 16) | (green << 8) | blue;
    }

    public static List<String> wrapText(TextRenderer textRenderer, String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");

        StringBuilder currentLine = new StringBuilder();
        for (String word : words) {
            StringBuilder testLine = new StringBuilder(currentLine);
            if (testLine.length() > 0) {
                testLine.append(" ");
            }
            testLine.append(word);

            if (textRenderer.getWidth(testLine.toString()) <= maxWidth) {
                currentLine = testLine;
            } else {

                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            }
        }
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    public static void shakeScreen(int duration, float i1, float i2) {
        ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(duration).setIntensity(i1, i2).setEasing(Easing.EXPO_OUT));
    }

    public static void addObjective(String text, PlayerEntity player){
        synchronized (DialogueManager.windows) {
            for (IDialogueWindow window : DialogueManager.windows) {
                if (window instanceof ObjectiveWindow && !window.isDone()) {
                    DialogueManager.windows.remove(window);
                    break;
                }
            }
        }

        DialogueManager.addDialogueWindow(new ObjectiveWindow(MinecraftClient.getInstance(), text));
        player.getWorld().playSound(null, BlockPos.ofFloored(player.getPos()), ModSounds.OBJECTIVE_SOUND_EFFECT, SoundCategory.PLAYERS, 1f, 1.2f);
    }

    public static void addDialogue(String text, PlayerEntity player) {
        synchronized (DialogueManager.windows) {
            for (IDialogueWindow window : DialogueManager.windows) {
                if (window instanceof DialogueWindow && !window.isDone()) {
                    DialogueManager.windows.remove(window);
                    break;
                }
            }
        }

        int typingSpeed = calculateTypingSpeed(text.length(), 50, 100);
        DialogueManager.addDialogueWindow(new DialogueWindow(MinecraftClient.getInstance(), text, 5, typingSpeed));

    }

    private static int calculateTypingSpeed(int textLength, int minSpeed, int maxSpeed) {
        int shortTextLengthThreshold = 50;
        int longTextLengthThreshold = 200;

        if (textLength <= shortTextLengthThreshold) {
            return maxSpeed;
        } else if (textLength >= longTextLengthThreshold) {
            return minSpeed;
        } else {
            float fraction = (float) (textLength - shortTextLengthThreshold) / (longTextLengthThreshold - shortTextLengthThreshold);
            return maxSpeed - (int) (fraction * (maxSpeed - minSpeed));
        }
    }
}
