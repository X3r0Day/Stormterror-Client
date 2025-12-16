package net.xyressa.stormterror.utils;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

public class MoveUtils {

    // Checks if the player is pressing movement keys
    public static boolean isMoving(PlayerEntity player) {
        if (player instanceof ClientPlayerEntity clientPlayer) {
            return clientPlayer.input.movementForward != 0 || clientPlayer.input.movementSideways != 0;
        }
        return false;
    }

    // Gets current speed
    public static double getSpeed(PlayerEntity player) {
        return Math.hypot(player.getVelocity().x, player.getVelocity().z);
    }

    // Sets velocity based on current yaw and intended speed
    public static void setMoveSpeed(PlayerEntity player, double speed) {
        if (player instanceof ClientPlayerEntity clientPlayer) {
            float forward = clientPlayer.input.movementForward;
            float strafe = clientPlayer.input.movementSideways;
            float yaw = player.getYaw();

            if (forward == 0 && strafe == 0) {
                player.setVelocity(0, player.getVelocity().y, 0);
            } else {
                if (forward != 0) {
                    if (strafe > 0) yaw += (forward > 0 ? -45 : 45);
                    else if (strafe < 0) yaw += (forward > 0 ? 45 : -45);
                    strafe = 0;
                    if (forward > 0) forward = 1;
                    else if (forward < 0) forward = -1;
                }
                double rad = Math.toRadians(yaw + 90);
                double x = forward * speed * Math.cos(rad) + strafe * speed * Math.sin(rad);
                double z = forward * speed * Math.sin(rad) - strafe * speed * Math.cos(rad);
                player.setVelocity(x, player.getVelocity().y, z);
            }
        }
    }
}