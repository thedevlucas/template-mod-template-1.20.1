package com.example.particles;

import com.example.particles.types.PingParticleType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.easing.Easing;

@Environment(EnvType.CLIENT)
public class PingParticle extends SpriteBillboardParticle {
    public PingParticle(ClientWorld clientLevel, double x, double y, double z, SpriteProvider spriteProvider) {
        super(clientLevel, x, y, z);
        setSpriteForAge(spriteProvider);
        this.maxAge = 1;
        this.scale = 0.05F;
    }

    public void method_3070() {
        this.scale = 0.05F;
        if (this.maxAge-- <= 0) {
            markDead();
        }
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void method_3074(VertexConsumer buffer, Camera camera, float partialTicks) {
        Quaternionf quaternionf;
        Vec3d vec3 = camera.getPos();
        float x = (float)(MathHelper.lerp(partialTicks, this.prevPosX, this.x) - vec3.getX());
        float y = (float)(MathHelper.lerp(partialTicks, this.prevPosY, this.y) - vec3.getY());
        float z = (float)(MathHelper.lerp(partialTicks, this.prevPosZ, this.z) - vec3.getZ());
        float d = (float)Math.sqrt((x * x + y * y + z * z));
        if (this.angle == 0.0F) {
            quaternionf = camera.getRotation();
        } else {
            quaternionf = new Quaternionf((Quaternionfc)camera.getRotation());
            quaternionf.rotateZ(MathHelper.lerp(partialTicks, this.prevAngle, this.angle));
        }
        Vector3f[] vector3fs = { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
        float i = getSize(partialTicks);
        for (int j = 0; j < 4; j++) {
            Vector3f vector3f = vector3fs[j];
            vector3f.rotate((Quaternionfc)quaternionf);
            vector3f.mul(i);
            vector3f.mul(d);
            vector3f.add(x, y, z);
        }
        float k = getMinU();
        float l = getMaxU();
        float m = getMinV();
        float n = getMaxV();
        int o = 15728880;
        float fadeDist = 40.0F;
        float alpha = this.alpha;
        if (d <= fadeDist) {
            alpha = MathHelper.clamp(this.alpha - MathHelper.lerp(Easing.SINE_IN.ease(d / fadeDist, 0.0F, 1.0F, 1.0F), 1.0F, 0.0F), 0.0F, 1.0F);
        }
        buffer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).texture(l, n).color(this.red, this.green, this.blue, alpha).light(o).next();
        buffer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).texture(l, m).color(this.red, this.green, this.blue, alpha).light(o).next();
        buffer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).texture(k, m).color(this.red, this.green, this.blue, alpha).light(o).next();
        buffer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).texture(k, n).color(this.red, this.green, this.blue, alpha).light(o).next();
    }


    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public DefaultFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            PingParticle instance = new PingParticle(world, x, y, z, this.spriteProvider);
            if (parameters instanceof PingParticleType) { PingParticleType pingParameters = (PingParticleType)parameters; if (pingParameters.initialData != null) {
                instance.red = (pingParameters.initialData.color >> 16 & 0xFF) / 255.0F;
                instance.green = (pingParameters.initialData.color >> 8 & 0xFF) / 255.0F;
                instance.blue = (pingParameters.initialData.color & 0xFF) / 255.0F;
            }  }
            return (Particle) instance;
        }
    }
}
