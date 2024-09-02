package com.example.shaders.post;

import com.example.TemplateMod;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Optional;
import ladysnake.satin.api.event.PostWorldRenderCallbackV2;
import ladysnake.satin.api.experimental.ReadableDepthFramebuffer;
import ladysnake.satin.api.managed.ManagedCoreShader;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import ladysnake.satin.api.managed.uniform.Uniform3f;
import ladysnake.satin.api.managed.uniform.UniformMat4;
import ladysnake.satin.api.util.GlMatrices;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.easing.Easing;

public final class BlackHoleRenderer {
    static {
        BLACK_HOLE_SHADER = ShaderEffectManager.getInstance().manageCoreShader(new Identifier(TemplateMod.MOD_ID, "black_hole"), VertexFormats.POSITION, shader -> {
            Framebuffer mainRenderTarget = MinecraftClient.getInstance().getFramebuffer();
            shader.findSampler("NoiseSampler").set(MinecraftClient.getInstance().getTextureManager().getTexture(new Identifier(TemplateMod.MOD_ID, "textures/vfx/noise.png")));
            shader.findSampler("DepthSampler").set(((ReadableDepthFramebuffer)mainRenderTarget).getStillDepthMap());
            shader.findUniform2f("OutSize").set(mainRenderTarget.textureWidth, mainRenderTarget.textureHeight);
            shader.findUniform4i("ViewPort").set(0, 0, MinecraftClient.getInstance().getWindow().getFramebufferWidth(), MinecraftClient.getInstance().getWindow().getFramebufferHeight());
        });
    }
    private static final ManagedCoreShader BLACK_HOLE_SHADER;
    private static final Uniform1f STIME = BLACK_HOLE_SHADER.findUniform1f("STime");
    private static final Uniform1f BLACK_HOLE_SCALE = BLACK_HOLE_SHADER.findUniform1f("BlackHoleScale");
    private static final Uniform1f BREAKING_PROGRESS = BLACK_HOLE_SHADER.findUniform1f("BreakingProgress");
    private static final Uniform3f CAMERA_POSITION = BLACK_HOLE_SHADER.findUniform3f("CameraPosition");
    private static final Uniform3f CAMERA_FRONT = BLACK_HOLE_SHADER.findUniform3f("CameraFront");
    private static final Uniform3f CAMERA_LEFT = BLACK_HOLE_SHADER.findUniform3f("CameraLeft");
    private static final Uniform3f CAMERA_UP = BLACK_HOLE_SHADER.findUniform3f("CameraUp");
    private static final UniformMat4 INVERSE_TRANSFORM_MATRIX = BLACK_HOLE_SHADER.findUniformMat4("InverseTransformMatrix");
    private static final Matrix4f projectionMatrix = new Matrix4f();

    @Nullable
    private static CoreEntity core;


    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> core = Optional.ofNullable(MinecraftClient.getInstance().player).map(player -> new CoreEntity(player.getType(), player.getWorld())).orElse(null));

        PostWorldRenderCallbackV2.EVENT.register((matrices, camera, tickDelta, nanoTime) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world != null && core != null) {
                Vec3d corePosition = core.getPos().add(0.0D, (core.getHeight() / 2.0F) + 150, 0.0D);
                INVERSE_TRANSFORM_MATRIX.set(GlMatrices.getInverseTransformMatrix(projectionMatrix));
                CAMERA_POSITION.set((float)(camera.getPos().getX() - corePosition.getX()), (float)(camera.getPos().getY() - corePosition.getY()), (float)(camera.getPos().getZ() - corePosition.getZ()));
                setNearPlaneUniforms(camera, tickDelta);
                STIME.set(((float)(client.world.getTime() % 2400000L) + tickDelta) / 20.0F);
                BLACK_HOLE_SCALE.set(Easing.CUBIC_IN.ease(MathHelper.clamp(core.getBreakingTicks() / 400.0F, 0.0F, 1.0F), 80.0F, 400.0F, 1.0F));
                BREAKING_PROGRESS.set(Easing.EXPO_IN.ease(MathHelper.clamp(core.getBreakingTicks() / 400.0F, 0.0F, 1.0F), 0.0F, 10.0F, 1.0F));
                render(matrices, client);
            }
        });
    }

    private static void setNearPlaneUniforms(Camera camera, float partialTicks) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        double windowRatio = (double) minecraft.getWindow().getFramebufferWidth() / minecraft.getWindow().getFramebufferHeight();
        double fov = Math.tan(((float)minecraft.gameRenderer.getFov(camera, partialTicks, true) * 0.017453292F) / 2.0D) * 0.05000000074505806D;
        double finalRatio = fov * windowRatio;
        Vector3f front = camera.getHorizontalPlane().mul(0.05F, new Vector3f());
        Vector3f left = camera.getDiagonalPlane().mul((float)finalRatio, new Vector3f());
        Vector3f up = camera.getVerticalPlane().mul((float)fov);
        CAMERA_FRONT.set(front.x(), front.y(), front.z());
        CAMERA_LEFT.set(left.x(), left.y(), left.z());
        CAMERA_UP.set(up.x(), up.y(), up.z());
    }

    private static void render(MatrixStack matrices, MinecraftClient client) {
        Matrix4f formerProjectionMatrix = RenderSystem.getProjectionMatrix();
        Matrix4f newProjectionMatrix = (new Matrix4f()).setOrtho(0.0F,
                (MinecraftClient.getInstance().getFramebuffer()).textureWidth,
                (MinecraftClient.getInstance().getFramebuffer()).textureHeight, 0.0F, 1000.0F, 3000.0F);

        RenderSystem.setProjectionMatrix(newProjectionMatrix, RenderSystem.getVertexSorting());
        matrices.push();
        matrices.loadIdentity();

        ShaderProgram shaderInstance = BLACK_HOLE_SHADER.getProgram();

        if (shaderInstance == null)
            return;
        if (shaderInstance.modelViewMat != null) {
            shaderInstance.modelViewMat.set((new Matrix4f()).translation(0.0F, 0.0F, -2000.0F));
        }

        if (shaderInstance.projectionMat != null) {
            shaderInstance.projectionMat.set(newProjectionMatrix);
        }

        shaderInstance.bind();
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        Tessellator tesselator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tesselator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        int height = (client.getFramebuffer()).textureHeight;
        int width = (client.getFramebuffer()).textureWidth;

        bufferBuilder.vertex(0.0D, height, 0.0D).next();
        bufferBuilder.vertex(width, height, 0.0D).next();
        bufferBuilder.vertex(width, 0.0D, 0.0D).next();
        bufferBuilder.vertex(0.0D, 0.0D, 0.0D).next();
        BufferRenderer.draw(bufferBuilder.end());
        shaderInstance.unbind();
        matrices.pop();
        RenderSystem.setProjectionMatrix(formerProjectionMatrix, RenderSystem.getVertexSorting());
    }
}