package net.xyressa.stormterror.modules.render;

import net.minecraft.client.input.Input;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.xyressa.stormterror.system.Category;
import net.xyressa.stormterror.system.Module;
import net.xyressa.stormterror.system.settings.NumberSetting;

public class Freecam extends Module {

    public static Freecam INSTANCE;

    public final NumberSetting speed = new NumberSetting("Speed", 0.1, 5.0, 1.0);

    // Ghost Camera State
    private double x, y, z;
    private double prevX, prevY, prevZ;
    private float yaw, pitch;
    private float prevYaw, prevPitch;

    // Backup the real input so we can restore it
    private Input originalInput;

    public Freecam() {
        super("Freecam", "Move camera freely while body stays put.", Category.RENDER);
        addSetting(speed);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (mc.player == null) return;

        // 1. Capture Render Position
        Vec3d camPos = mc.gameRenderer.getCamera().getPos();
        x = prevX = camPos.x;
        y = prevY = camPos.y;
        z = prevZ = camPos.z;
        yaw = prevYaw = mc.player.getYaw();
        pitch = prevPitch = mc.player.getPitch();

        // 2. Freeze the Player Body (Swap Input)
        originalInput = mc.player.input;
        mc.player.input = new Input(); // Empty input means no movement
    }

    @Override
    public void onDisable() {
        // Restore the real input so the player can move again
        if (mc.player != null && originalInput != null) {
            mc.player.input = originalInput;
        }
        originalInput = null;
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        // Ensure the player stays frozen (just in case game resets input)
        if (!(mc.player.input instanceof Input) || mc.player.input.getClass() != Input.class) {
            // If input changed back to KeyboardInput unexpectedly, swap it again
            // But usually onEnable is enough.
        }

        // 1. Interpolation
        prevX = x; prevY = y; prevZ = z;
        prevYaw = yaw; prevPitch = pitch;

        // 2. Calculate Movement (Using hardware keys directly)
        float yawRad = (float) Math.toRadians(yaw);
        float pitchRad = (float) Math.toRadians(pitch);
        double vX = 0, vY = 0, vZ = 0;
        double s = speed.getValue();

        if (mc.options.sprintKey.isPressed()) s *= 2.0;

        if (mc.options.forwardKey.isPressed()) {
            vX += -Math.sin(yawRad) * Math.cos(pitchRad) * s;
            vZ += Math.cos(yawRad) * Math.cos(pitchRad) * s;
            vY += -Math.sin(pitchRad) * s;
        }
        if (mc.options.backKey.isPressed()) {
            vX += Math.sin(yawRad) * Math.cos(pitchRad) * s;
            vZ += -Math.cos(yawRad) * Math.cos(pitchRad) * s;
            vY += Math.sin(pitchRad) * s;
        }
        if (mc.options.leftKey.isPressed()) {
            vX += Math.cos(yawRad) * s;
            vZ += Math.sin(yawRad) * s;
        }
        if (mc.options.rightKey.isPressed()) {
            vX += -Math.cos(yawRad) * s;
            vZ += -Math.sin(yawRad) * s;
        }
        if (mc.options.jumpKey.isPressed()) vY += s;
        if (mc.options.sneakKey.isPressed()) vY -= s;

        x += vX; y += vY; z += vZ;
    }

    public void changeLookDirection(double dx, double dy) {
        yaw += dx;
        pitch += dy;
        pitch = MathHelper.clamp(pitch, -90, 90);
    }

    public double getX(float tickDelta) { return MathHelper.lerp(tickDelta, prevX, x); }
    public double getY(float tickDelta) { return MathHelper.lerp(tickDelta, prevY, y); }
    public double getZ(float tickDelta) { return MathHelper.lerp(tickDelta, prevZ, z); }
    public float getYaw(float tickDelta) { return MathHelper.lerp(tickDelta, prevYaw, yaw); }
    public float getPitch(float tickDelta) { return MathHelper.lerp(tickDelta, prevPitch, pitch); }
}