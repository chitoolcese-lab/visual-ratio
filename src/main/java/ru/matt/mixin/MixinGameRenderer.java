package ru.matt.mixin;

import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import ru.matt.config.VisualRatioConfig;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @ModifyArg(
            method = "getBasicProjectionMatrix",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/Matrix4f;viewboxMatrix(DFFF)Lnet/minecraft/util/math/Matrix4f;"
            ),
            index = 1
    )
    private float visualratio$modifyAspectRatio(float aspectRatio) {
        if (VisualRatioConfig.enabled) {
            return VisualRatioConfig.visualratioLog;
        }
        return aspectRatio;
    }
}
