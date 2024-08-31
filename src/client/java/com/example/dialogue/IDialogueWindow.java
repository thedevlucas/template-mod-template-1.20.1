package com.example.dialogue;

import net.minecraft.client.gui.DrawContext;

public interface IDialogueWindow {
    void render(DrawContext context, float tickDelta);
    boolean isDone();
}