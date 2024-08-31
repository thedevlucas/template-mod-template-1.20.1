package com.example.dialogue;

import com.example.TemplateModClient;
import com.example.Utilities;
import com.example.particles.ParticleRegister;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.registry.common.particle.LodestoneScreenParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.ScreenParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;
import team.lodestar.lodestone.systems.particle.screen.base.ScreenParticle;

import java.awt.*;

import static net.minecraft.util.math.MathHelper.lerp;

public class DeathWindow implements IDialogueWindow {
    private final MinecraftClient client;
    private long lastUpdateTime;
    private float totalTickDelta;
    private float transitionProgress = 0.0f;
    public static final ScreenParticleHolder SCREEN_PARTICLES = new ScreenParticleHolder();

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
