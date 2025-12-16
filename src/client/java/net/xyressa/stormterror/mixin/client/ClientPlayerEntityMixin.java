package net.xyressa.stormterror.mixin.client;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.xyressa.stormterror.modules.render.Freecam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    private void onChangeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        if ((Object)this instanceof ClientPlayerEntity) {
            if (Freecam.INSTANCE != null && Freecam.INSTANCE.isEnabled()) {
                Freecam.INSTANCE.changeLookDirection(cursorDeltaX, cursorDeltaY);
                ci.cancel();
            }
        }
    }
}