package com.example;

import com.example.dialogue.*;
import com.example.sound.ModSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.ChatVisibility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

import java.util.ArrayList;
import java.util.List;

public class Utilities {
    public static boolean shouldRender = true;

    public static void toggleRender(boolean value, MinecraftClient client) {
        shouldRender = value;
        if (!value){
            client.options.getChatVisibility().setValue(ChatVisibility.HIDDEN);
        } else {
            client.options.getChatVisibility().setValue(ChatVisibility.FULL);
        }
    }

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
            if (!testLine.isEmpty()) {
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
        if (!currentLine.isEmpty()) {
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
                if (window instanceof ObjectiveWindow && !window.isDone() || window instanceof ObjectiveFailed || window instanceof ObjectiveComplete) {
                    DialogueManager.windows.remove(window);
                    break;
                }
            }
        }

        DialogueManager.addDialogueWindow(new ObjectiveWindow(MinecraftClient.getInstance(), toSmallCaps(text)));
        player.getWorld().playSound(null, BlockPos.ofFloored(player.getPos()), ModSounds.SWOOSH_SOUND_EFFECT, SoundCategory.PLAYERS, 1f, 1.2f);
        player.getWorld().playSound(null, BlockPos.ofFloored(player.getPos()), ModSounds.OBJECTIVE_SOUND_EFFECT, SoundCategory.PLAYERS, 1f, 1.2f);


    }

    public static void addCompletedObjective(PlayerEntity player){
        synchronized (DialogueManager.windows) {
            for (IDialogueWindow window : DialogueManager.windows) {
                if (window instanceof ObjectiveComplete && !window.isDone() || window instanceof ObjectiveWindow || window instanceof ObjectiveFailed) {
                    DialogueManager.windows.remove(window);
                    break;
                }
            }
        }

        DialogueManager.addDialogueWindow(new ObjectiveComplete(MinecraftClient.getInstance()));
        player.getWorld().playSound(null, BlockPos.ofFloored(player.getPos()), ModSounds.SWOOSH_SOUND_EFFECT, SoundCategory.PLAYERS, 1f, 1.2f);
        player.getWorld().playSound(null, BlockPos.ofFloored(player.getPos()), ModSounds.OBJECTIVE_COMPLETE_SOUND, SoundCategory.PLAYERS, 1f, 1.2f);
    }

    public static void addOFailedbjective(PlayerEntity player){
        synchronized (DialogueManager.windows) {
            for (IDialogueWindow window : DialogueManager.windows) {
                if (window instanceof ObjectiveFailed && !window.isDone() || window instanceof ObjectiveWindow || window instanceof ObjectiveComplete) {
                    DialogueManager.windows.remove(window);
                    break;
                }
            }
        }

        DialogueManager.addDialogueWindow(new ObjectiveFailed(MinecraftClient.getInstance()));
        player.getWorld().playSound(null, BlockPos.ofFloored(player.getPos()), ModSounds.SWOOSH_SOUND_EFFECT, SoundCategory.PLAYERS, 1f, 1.2f);
        player.getWorld().playSound(null, BlockPos.ofFloored(player.getPos()), ModSounds.OBJECTIVE_FAILED_SOUND, SoundCategory.PLAYERS, 1f, 1.2f);
    }

    public static void addDialogue(String text) {
        synchronized (DialogueManager.windows) {
            for (IDialogueWindow window : DialogueManager.windows) {
                if (window instanceof DialogueWindow && !window.isDone()) {
                    DialogueManager.windows.remove(window);
                    break;
                }
            }
        }

        int typingSpeed = calculateTypingSpeed(text.length());
        DialogueManager.addDialogueWindow(new DialogueWindow(MinecraftClient.getInstance(), text, 5, typingSpeed));
        

    }

    private static int calculateTypingSpeed(int textLength) {
        int shortTextLengthThreshold = 50;
        int longTextLengthThreshold = 200;

        if (textLength <= shortTextLengthThreshold) {
            return 100;
        } else if (textLength >= longTextLengthThreshold) {
            return 50;
        } else {
            float fraction = (float) (textLength - shortTextLengthThreshold) / (longTextLengthThreshold - shortTextLengthThreshold);
            return 100 - (int) (fraction * (100 - 50));
        }
    }

    public static String toSmallCaps(String text) {
        StringBuilder smallCapsText = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (Character.isLowerCase(c)) {
                smallCapsText.append(convertToSmallCaps(c));
            } else if (Character.isUpperCase(c)) {
                smallCapsText.append(convertToSmallCaps(Character.toLowerCase(c)));
            } else {
                smallCapsText.append(c);
            }
        }

        return smallCapsText.toString();
    }

    private static char convertToSmallCaps(char c) {
        return switch (c) {
            case 'a' -> 'ᴀ';
            case 'b' -> 'ʙ';
            case 'c' -> 'ᴄ';
            case 'd' -> 'ᴅ';
            case 'e' -> 'ᴇ';
            case 'f' -> 'ғ';
            case 'g' -> 'ɢ';
            case 'h' -> 'ʜ';
            case 'i' -> 'ɪ';
            case 'j' -> 'ᴊ';
            case 'k' -> 'ᴋ';
            case 'l' -> 'ʟ';
            case 'm' -> 'ᴍ';
            case 'n' -> 'ɴ';
            case 'o' -> 'ᴏ';
            case 'p' -> 'ᴘ';
            case 'q' -> 'ǫ';
            case 'r' -> 'ʀ';
            case 's' -> 's';
            case 't' -> 'ᴛ';
            case 'u' -> 'ᴜ';
            case 'v' -> 'ᴠ';
            case 'w' -> 'ᴡ';
            case 'x' -> 'x';
            case 'y' -> 'ʏ';
            case 'z' -> 'ᴢ';
            default -> c;
        };
    }
}
