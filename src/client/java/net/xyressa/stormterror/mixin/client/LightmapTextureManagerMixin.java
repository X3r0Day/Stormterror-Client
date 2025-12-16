package net.xyressa.stormterror.mixin.client;

import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.render.LightmapTextureManager;
import net.xyressa.stormterror.modules.render.Fullbright;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {

    /**
     * Redirects the gamma value retrieval.
     * If Fullbright is enabled, it returns 100.0 (Bright).
     * If disabled, it returns the normal user setting (0.0 - 1.0).
     */
    @Redirect(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/option/SimpleOption;getValue()Ljava/lang/Object;"
            )
    )
    private Object forceFullbrightGamma(SimpleOption<Double> instance) {
        // Access the static boolean we created in Fullbright.java
        if (Fullbright.fullbrightEnabled) {
            return 100.0;
        }
        // Return the normal gamma value
        return instance.getValue();
    }
}