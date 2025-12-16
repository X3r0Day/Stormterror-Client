package net.xyressa.stormterror.modules.movement;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.xyressa.stormterror.system.Category;
import net.xyressa.stormterror.system.Module;

public class NoFall extends Module {

    public NoFall() {
        super("NoFall", "Prevents fall damage.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        if (mc.player.fallDistance > 2.5f) {
            mc.player.networkHandler.sendPacket(
                    new PlayerMoveC2SPacket.OnGroundOnly(true, mc.player.horizontalCollision)
            );
            mc.player.fallDistance = 0;
        }
    }
}