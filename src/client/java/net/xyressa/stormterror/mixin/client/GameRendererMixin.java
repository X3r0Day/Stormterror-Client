package net.xyressa.stormterror.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.xyressa.stormterror.modules.render.Fullbright;
import net.xyressa.stormterror.system.ModuleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    private Double originalGamma = null; // Use wrapper Double to handle null checks

    // Hook BEFORE the world is rendered
    @Inject(method = "renderWorld", at = @At("HEAD"))
    private void onRenderWorldHead(CallbackInfo ci) {
        Fullbright fullbright = ModuleManager.INSTANCE.get(Fullbright.class);

        if (fullbright != null && fullbright.isEnabled()) {
            MinecraftClient mc = MinecraftClient.getInstance();

            // Save original gamma only if we haven't already (prevents overriding with 10.0)
            if (originalGamma == null) {
                originalGamma = mc.options.getGamma().getValue();
            }

            // Set gamma to 1000% (Bright!)
            mc.options.getGamma().setValue(10.0);
        }
    }

    // Hook AFTER the world is rendered to restore normal brightness for GUIs
    @Inject(method = "renderWorld", at = @At("TAIL"))
    private void onRenderWorldTail(CallbackInfo ci) {
        Fullbright fullbright = ModuleManager.INSTANCE.get(Fullbright.class);

        if (fullbright != null && fullbright.isEnabled() && originalGamma != null) {
            // Restore gamma so menus don't look washed out
            MinecraftClient.getInstance().options.getGamma().setValue(originalGamma);
            originalGamma = null; // Reset for next frame
        }
    }
}