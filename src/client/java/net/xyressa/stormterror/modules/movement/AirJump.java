package net.xyressa.stormterror.modules.movement;

import net.xyressa.stormterror.system.Category;
import net.xyressa.stormterror.system.Module;
import net.xyressa.stormterror.system.settings.BooleanSetting;
import net.xyressa.stormterror.system.settings.NumberSetting;
import net.xyressa.stormterror.utils.MoveUtils;

public class AirJump extends Module {

    // Define new settings
    private final NumberSetting multiplier = new NumberSetting("Multiplier", 0.5, 3.0, 1.0);
    private final BooleanSetting autoStrafe = new BooleanSetting("Auto Strafe", true);

    private boolean wasPressed = false;

    public AirJump() {
        super("AirJump", "Jumps in air. Duh.", Category.MOVEMENT);

        // Register the settings so they appear in the ClickGUI
        addSetting(multiplier);
        addSetting(autoStrafe);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        boolean isPressed = mc.options.jumpKey.isPressed();

        // Check if the key was just pressed (not held down) and we are in the air
        if (isPressed && !wasPressed && !mc.player.isOnGround()) {

            // 1. Vertical Logic (The Jump)
            // Reset Y velocity to 0 before adding force for consistent height
            mc.player.setVelocity(mc.player.getVelocity().x, 0, mc.player.getVelocity().z);

            // Calculate jump force using the setting
            double jumpVelocity = 0.42 * multiplier.getValue();
            mc.player.addVelocity(0, jumpVelocity, 0);

            // 2. Horizontal Logic (The Strafe)
            // Only apply if the "Auto Strafe" setting is enabled
            if (autoStrafe.getValue() && MoveUtils.isMoving(mc.player)) {
                // We want to keep our current speed, or at least sprint speed.
                // If we are moving slow, speed up to sprint speed (0.28).
                // If we are already fast (Speed module), keep that speed.
                double currentSpeed = MoveUtils.getSpeed(mc.player);
                double strafeSpeed = Math.max(currentSpeed, 0.2873);

                // Snap velocity to the keys pressed
                MoveUtils.setMoveSpeed(mc.player, strafeSpeed);
            }
        }

        wasPressed = isPressed;
    }
}