package ru.matt.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import ru.matt.config.VisualRatioConfig;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Shadow
    private MinecraftClient client;

    @Shadow
    private Camera camera;

    @ModifyVariable(method = "renderWorld", at = @At(value = "STORE"), ordinal = 0)
    private Matrix4f visualratio$modifyProjectionMatrix(Matrix4f matrix4f) {
        if (!VisualRatioConfig.enabled) {
            return matrix4f;
        }

        float realAspect = (float) this.client.getWindow().getFramebufferWidth()
                / (float) this.client.getWindow().getFramebufferHeight();

        // Reescala el termino horizontal de la matriz de proyeccion para simular
        // un aspect ratio distinto sin tocar el FOV real configurado por el jugador.
        matrix4f.a00 *= (realAspect / VisualRatioConfig.visualratioLog);

        return matrix4f;
    }
}
