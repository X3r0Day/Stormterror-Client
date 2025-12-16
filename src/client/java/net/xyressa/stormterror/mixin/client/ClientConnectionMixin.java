package net.xyressa.stormterror.mixin.client;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.xyressa.stormterror.mixin.PlayerMoveC2SPacketAccessor;
import net.xyressa.stormterror.modules.movement.NoFall;
import net.xyressa.stormterror.system.ModuleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"))
    private void onSend(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof PlayerMoveC2SPacket) {
            NoFall noFall = ModuleManager.INSTANCE.get(NoFall.class);

            // If NoFall is enabled, force onGround = true
            if (noFall != null && noFall.isEnabled()) {
                ((PlayerMoveC2SPacketAccessor) packet).setOnGround(true);
            }
        }
    }
}