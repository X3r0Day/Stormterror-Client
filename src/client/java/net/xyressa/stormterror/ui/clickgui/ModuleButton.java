package net.xyressa.stormterror.ui.clickgui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.xyressa.stormterror.system.Module;

public class ModuleButton {
    public final Module module;
    private final Panel parent;
    public int offset;

    public ModuleButton(Module module, Panel parent, int offset) {
        this.module = module;
        this.parent = parent;
        this.offset = offset;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int x = parent.x;
        int y = parent.y + offset;
        int width = parent.width;

        boolean hovered = isHovered(mouseX, mouseY);

        // Purple Enabled (0xFF8A2BE2), Dark Disabled (0xFF282828)
        int color = module.isEnabled() ? 0xFF8A2BE2 : 0xFF282828;

        // Hover effect (Lighter Purple / Lighter Gray)
        if (hovered) {
            color = module.isEnabled() ? 0xFFA050F0 : 0xFF3C3C3C;
        }

        context.fill(x, y, x + width, y + 15, color);
        context.drawText(MinecraftClient.getInstance().textRenderer, module.getName(), x + 4, y + 4, 0xFFFFFFFF, true);

        // Indicator for settings
        if (!module.getSettings().isEmpty()) {
            context.drawText(MinecraftClient.getInstance().textRenderer, "+", x + width - 10, y + 4, 0xFFAAAAAA, true);
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            if (button == 0) {
                module.toggle();
            }
            // Right click is handled by ClickGUIScreen to open SettingsWindow
        }
    }

    public int getHeight() {
        return 15;
    }

    public boolean isHovered(double mouseX, double mouseY) {
        int x = parent.x;
        int y = parent.y + offset;
        return mouseX >= x && mouseX <= x + parent.width && mouseY >= y && mouseY <= y + 15;
    }
}