package net.xyressa.stormterror.modules.movement;

import net.xyressa.stormterror.system.Category;
import net.xyressa.stormterror.system.Module;
import net.xyressa.stormterror.system.settings.NumberSetting;
import net.xyressa.stormterror.utils.MoveUtils;

public class Speed extends Module {

    public final NumberSetting speed = new NumberSetting("Speed", 0.1, 10.0, 2.0);

    public Speed() {
        super("Speed", "Go fast.", Category.MOVEMENT);
        addSetting(speed);
    }

    @Override
    public void onTick() {
        // 'mc' is now guaranteed safe by onTickWrapper
        if (mc.player == null) return;

        if (MoveUtils.isMoving(mc.player)) {
            MoveUtils.setMoveSpeed(mc.player, speed.getValue() * 0.2873);
        } else {
            mc.player.setVelocity(0, mc.player.getVelocity().y, 0);
        }
    }
}