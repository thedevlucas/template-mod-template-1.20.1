package com.example.dialogue;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

public class DialogueManager {
    public static final List<IDialogueWindow> windows = Collections.synchronizedList(new LinkedList<>());
    private static Timer timer = new Timer();

    public static void addDialogueWindow(IDialogueWindow window) {
        synchronized (windows) {
            windows.add(window);
        }
    }

    public static void renderAll(DrawContext context, float tickDelta) {
        List<IDialogueWindow> windowsToRender;

        synchronized (windows) {
            windowsToRender = new LinkedList<>(windows);
        }

        for (IDialogueWindow window : windowsToRender) {
            window.render(context, tickDelta);
            if (window.isDone()) {
                synchronized (windows) {
                    windows.remove(window);
                }
            }
        }
    }
}
