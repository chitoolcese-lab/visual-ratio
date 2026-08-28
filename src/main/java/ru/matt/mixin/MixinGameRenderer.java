package ru.matt.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import ru.matt.config.VisualRatioConfig;

import java.lang.reflect.Field;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Shadow
    private MinecraftClient client;

    private static Field visualratio$a00Field;

    private static float visualratio$getA00(Matrix4f matrix4f) {
        try {
            if (visualratio$a00Field == null) {
                visualratio$a00Field = Matrix4f.class.getDeclaredField("a00");
                visualratio$a00Field.setAccessible(true);
            }
            return visualratio$a00Field.getFloat(matrix4f);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private static void visualratio$setA00(Matrix4f matrix4f, float value) {
        try {
            if (visualratio$a00Field == null) {
                visualratio$a00Field = Matrix4f.class.getDeclaredField("a00");
                visualratio$a00Field.setAccessible(true);
            }
            visualratio$a00Field.setFloat(matrix4f, value);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @ModifyVariable(method = "renderWorld", at = @At(value = "STORE"), ordinal = 0)
    private Matrix4f visualratio$modifyProjectionMatrix(Matrix4f matrix4f) {
        if (!VisualRatioConfig.enabled) {
            return matrix4f;
        }

        try {
            float realAspect = (float) this.client.getWindow().getFramebufferWidth()
                    / (float) this.client.getWindow().getFramebufferHeight();

            float current = visualratio$getA00(matrix4f);
            visualratio$setA00(matrix4f, current * (realAspect / VisualRatioConfig.visualratioLog));
        } catch (Throwable t) {
            // Si algo falla (ej: el campo no se llama "a00" en este build de yarn),
            // no rompemos el juego, solo no aplicamos el efecto.
        }

        return matrix4f;
    }
}
