package com.example;

import com.example.dialogue.DialogueManager;
import com.example.dialogue.IDialogueWindow;
import com.example.dialogue.ObjectiveWindow;
import com.example.sound.ModSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.registry.common.particle.LodestoneScreenParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.ScreenParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneScreenParticleRenderType;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;
import team.lodestar.lodestone.systems.particle.screen.base.ScreenParticle;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderType;
import team.lodestar.lodestone.systems.rendering.StateShards;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry.builder;
import static team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry.createGenericRenderType;

public class Utilities {
    private static boolean shouldRender = true;
    public static final Identifier DEFAULT_SKIN_LOCATION = new Identifier(TemplateMod.MOD_ID,"textures/vfx/noise.png");
    public static final LodestoneRenderType RENDER_TYPE = createGenericRenderType("additive_block", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.TRIANGLES, builder().setTransparencyState(StateShards.NO_TRANSPARENCY));

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
        player.getWorld().playSound(null, BlockPos.ofFloored(player.getPos()), ModSounds.OBJECTIVE_SOUND_EFFECT, SoundCategory.PLAYERS, 1f, 1f);

    }

    public static void renderBeam(MatrixStack matrix, float x, float y, float z){
        VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld();
        builder.replaceBufferSource(RenderHandler.LATE_DELAYED_RENDER.getTarget())
                .setRenderType(RENDER_TYPE)
                .renderSphere(matrix, 0.5f, 20,20)
                .setAlpha(1.0F)
                .setColor(new Color(255));

        float halfSize = 5 / 2.0f;
        matrix.push();

        Matrix4f matrix4f = matrix.peek().getPositionMatrix();

        BlockPos startPos = new BlockPos((int) (2 - halfSize), (int) 5, (int) (5 - halfSize));
        BlockPos endPos = new BlockPos((int) (1 + halfSize), (int) 5 + 300, (int) (5 + halfSize));
        builder.renderBeam(matrix4f, startPos, endPos, 1);
        matrix.pop();
        TemplateMod.LOGGER.info(String.valueOf(startPos));
    }

    public static void spawnParticles() {
        float time = MinecraftClient.getInstance().world.getTime() + System.currentTimeMillis();
        Color firstColor = new Color(255);
        Color secondColor = new Color(0,255,255);
        ScreenParticleBuilder.create(LodestoneScreenParticleRegistry.STAR, new ScreenParticleHolder())
                .setTransparencyData(GenericParticleData.create(0.09f*5, 0f).setEasing(Easing.QUINTIC_IN).build())
                .setScaleData(GenericParticleData.create((float) (1.5f + Math.sin(time * 0.1f) * 0.125f), 0).build())
                .setColorData(ColorParticleData.create(firstColor, secondColor).setCoefficient(1.25f).build())
                .setLifetime(6)
                .setRandomOffset(0.05f)
                .setScaleData(GenericParticleData.create((float) (1.4f - Math.sin(time * 0.075f) * 0.125f), 0).build())
                .setColorData(ColorParticleData.create(secondColor, firstColor).build())
                .spawn(0,0);
    }


}
