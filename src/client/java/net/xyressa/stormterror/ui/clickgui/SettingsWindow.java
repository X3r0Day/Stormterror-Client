package net.xyressa.stormterror.ui.clickgui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.xyressa.stormterror.system.Module;
import net.xyressa.stormterror.system.settings.BooleanSetting;
import net.xyressa.stormterror.system.settings.ModeSetting;
import net.xyressa.stormterror.system.settings.NumberSetting;
import net.xyressa.stormterror.system.settings.Setting;

// DO NOT IMPORT java.awt.Color

public class SettingsWindow {
    public int x, y, width, height;
    public final Module module;
    private boolean dragging = false;
    private int dragX, dragY;
    private boolean isSliding = false;

    public SettingsWindow(Module module, int x, int y) {
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = 120;
        this.height = 20;
    }

    public int getTotalHeight() {
        int h = height;
        for (Setting<?> setting : module.getSettings()) {
            if (setting.isVisible()) h += 20;
        }
        return h;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }

        // 1. Header (Dark Grey: 0xFF1E1E1E)
        context.fill(x, y, x + width, y + height, 0xFF1E1E1E);
        context.drawText(MinecraftClient.getInstance().textRenderer, module.getName() + " Settings", x + 5, y + 6, 0xFFFFFFFF, true);

        // 2. Settings Background (Transparent Black: 0xC8141414)
        int settingsHeight = getTotalHeight() - height;
        if (settingsHeight > 0) {
            context.fill(x, y + height, x + width, y + height + settingsHeight, 0xC8141414);
        }

        int offsetY = height;
        for (Setting<?> setting : module.getSettings()) {
            if (!setting.isVisible()) continue;

            int settingY = y + offsetY;
            int settingX = x;

            if (setting instanceof BooleanSetting boolSet) {
                context.drawText(MinecraftClient.getInstance().textRenderer, boolSet.getName(), settingX + 5, settingY + 6, 0xFFFFFFFF, true);
                // Green: 0xFF64FF64, Red: 0xFFFF6464
                int color = boolSet.getValue() ? 0xFF64FF64 : 0xFFFF6464;
                context.fill(settingX + width - 15, settingY + 5, settingX + width - 5, settingY + 15, color);
            }
            else if (setting instanceof NumberSetting numSet) {
                double min = numSet.getMin();
                double max = numSet.getMax();
                double val = numSet.getValue();

                if (isSliding && mouseX >= settingX && mouseX <= settingX + width && mouseY >= settingY && mouseY <= settingY + 20) {
                    double diff = Math.min(width, Math.max(0, mouseX - settingX));
                    double newVal = min + (diff / width) * (max - min);
                    numSet.setValue(newVal);
                }

                // Slider Background
                context.fill(settingX, settingY, settingX + width, settingY + 20, 0xFF282828);
                // Slider Bar (Purple: 0xFF8A2BE2)
                int sliderWidth = (int) ((val - min) / (max - min) * width);
                context.fill(settingX, settingY, settingX + sliderWidth, settingY + 20, 0xFF8A2BE2);

                String display = numSet.getName() + ": " + String.format("%.2f", val);
                context.drawText(MinecraftClient.getInstance().textRenderer, display, settingX + 5, settingY + 6, 0xFFFFFFFF, true);
            }
            else if (setting instanceof ModeSetting modeSet) {
                context.drawText(MinecraftClient.getInstance().textRenderer, modeSet.getName() + ": " + modeSet.getValue(), settingX + 5, settingY + 6, 0xFFAAAAAA, true);
            }

            offsetY += 20;
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            if (button == 0) {
                dragging = true;
                dragX = (int) (mouseX - x);
                dragY = (int) (mouseY - y);
                return;
            }
        }

        int offsetY = height;
        for (Setting<?> setting : module.getSettings()) {
            if (!setting.isVisible()) continue;

            int settingY = y + offsetY;
            if (mouseX >= x && mouseX <= x + width && mouseY >= settingY && mouseY < settingY + 20) {
                if (setting instanceof BooleanSetting boolSet) {
                    boolSet.toggle();
                } else if (setting instanceof ModeSetting modeSet) {
                    modeSet.cycle();
                } else if (setting instanceof NumberSetting) {
                    isSliding = true;
                }
            }
            offsetY += 20;
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        isSliding = false;
    }
}